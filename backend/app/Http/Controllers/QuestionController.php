<?php

namespace App\Http\Controllers;

use Auth;
use App\User;
use App\Question;
use Illuminate\Http\Request;
use App\Repositories\ImageUploadRepository;

class QuestionController extends Controller
{
    protected $image;

    public function __construct(ImageUploadRepository $image)
    {
        $this->image = $image;
    }

    /**
     * @api {get} /questions 列出所有 Questions
     * @apiGroup Questions
     *
     * @apiParam {String} [school_id]
     * @apiParam {String} [tag_id]
     * @apiParam {Number=1,0} [solved]
     * @apiParam (VolunteerParam) {String} [grade]
     * @apiParam (VolunteerParam) {Number=1,0} [has_digitalized_question]
     * @apiParam (VolunteerParam) {String=created_at,updated_at} [order_by=updated_at]
     * @apiSuccess (StudentScope) {Object[]} data Questions
     * @apiSuccess (VolunteerScope) {Object[]} data Questions
     * @apiUse UnauthorizedError
     */
    public function index(Request $request)
    {
        $user = Auth::guard('api')->user();

        if ($user->isAdmin()) {
            // TODO
            return [];
        }

        if ($user->isVolunteer()) {
            return $this->_indexForVolunteer($request);
        }

        if ($user->isStudent()) {
            return $this->_indexForStudent($request);
        }
    }

    public function _indexForVolunteer(Request $request)
    {
        $query = Question::with([
            'images',
            'user',
            'user.userable',
            'school',
            'tags',
            'digitalized_question',
        ]);

        if ($request->has('school_id')) {
            $query = $query->inSchools($request->school_id);
        }

        if ($request->has('tag_id')) {
            $tag_id = explode(',', $request->tag_id);
            $query = $query->whereHas('tags', function ($q) use ($tag_id) {
                $q->whereIn('tag_id', $tag_id);
            });
        }

        if ($request->has('solved')) {
            $query = $query->where('solved', $request->solved === '1' ? true : false);
        }

        if ($request->has('grade')) {
            $query = $query->where('grade', $request->grade);
        }

        if ($request->has('has_digitalized_question')) {
            if ($request->has_digitalized_question === '1') {
                $query = $query->has('digitalized_question');
            } else {
                $query = $query->has('digitalized_question', '=', 0);
            }
        }

        if ($request->has('order_by')) {
            switch ($request->order_by) {
            case 'created_at':
                $query = $query->orderBy('created_at', 'desc');
                break;
            case 'updated_at':
            default:
                $query = $query->orderBy('updated_at', 'desc');
                break;
            }
        }

        return $query->paginate(15);
    }

    public function _indexForStudent(Request $request)
    {
        $query = Question::with([
            'images',
            'user',
            'user.userable',
            'school',
            'tags',
            'digitalized_question',
        ]);

        if ($request->has('mine') && $request->mine === '1') {
            $user = Auth::guard('api')->user();

            $query->where('user_id', $user->id);
        }

        if ($request->has('school_id')) {
            $query = $query->inSchools($request->school_id);
        }

        if ($request->has('tag_id')) {
            $tag_id = explode(',', $request->tag_id);
            $query = $query->whereHas('tags', function ($q) use ($tag_id) {
                $q->whereIn('tag_id', $tag_id);
            });
        }

        if ($request->has('solved')) {
            $query = $query->where('solved', $request->solved === '1' ? true : false);
        }

        $query = $query->orderBy('updated_at', 'desc');

        return $query->paginate(15);
    }

    public function show($question_id)
    {
        $question = Question::with([
            'user', 'user.userable', 'school', 'images', 'answer', 'tags',
            'digitalized_question',
        ])->findOrFail($question_id);

        $student = $question->user->userable;
        $student->school;

        return $question;
    }

    /**
     * @api {post} /questions 新增 Question
     * @apiGroup Questions
     *
     * @apiParam {String} content
     * @apiParam (AdminParam) {Number} user_id 學生的 user_id
     * @apiParam {Image[]} [files] 問題的圖片
     * @apiSuccess {Number} id Question id
     * @apiSuccess {Object[]} images
     * @apiUse UnauthorizedError
     * @apiUse ValidationError
     */
    public function store(Request $request)
    {
        $this->validate($request, [
            'content' => 'required',
        ]);

        // TODO determined the user
        $user = Auth::guard('api')->user();

        $question = new Question();
        $question->type = Question::MATH;
        $question->content = $request->content;
        $question->solved = false;

        if ($user->isStudent()) {
            $question->school_id = $user->userable->school_id;
            $question->grade = $user->userable->grade;

            $user->questions()->save($question);
        } else {
            $user_id = $request->input('user_id');
            $studentUser = User::findOrFail($user_id);

            $question->school_id = $studentUser->userable->school_id;
            $question->grade = $studentUser->userable->grade;

            $studentUser->questions()->save($question);
        }

        if ($request->hasFile('files')) {
            $files = $request->file('files');

            foreach ($files as $file) {
                $image = $this->image->createImageFromUploadedFile($file);
                $question->images()->save($image);
            }
        }

        return $question->load('images');
    }

    public function update(Request $request, $question_id)
    {
        $this->validate($request, [
            'content' => 'required',
        ]);

        $question = Question::findOrFail($question_id);
        $question->content = $request->content;

        $question->save();

        return $question;
    }

    public function updateSolved(Request $request, $question_id)
    {
        $this->validate($request, [
            'solved' => 'in:0,1',
        ]);

        $question = Question::findOrFail($question_id);
        $question->solved = $request->solved;

        $question->save();

        return ['status' => 'success'];
    }

    public function destroy($question_id)
    {
        $question = Question::findOrFail($question_id);

        $question->images()->delete();
        $question->delete();

        return ['status' => 'success'];
    }
}
