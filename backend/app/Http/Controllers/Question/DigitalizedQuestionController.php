<?php

namespace App\Http\Controllers\Question;

use Auth;
use App\Question;
use App\DigitalizedQuestion;
use Illuminate\Http\Request;
use App\Http\Controllers\ApiController;
use App\Exceptions\ResourceException;
use Illuminate\Database\QueryException;

class DigitalizedQuestionController extends ApiController
{
    /**
     * @api {post} /questions/:id/digitalized_question 新增數位化問題
     * @apiGroup Questions
     *
     * @apiPermission Admin
     * @apiPermission Volunteer
     *
     * @apiParam {String} content
     * @apiSuccess {Number} id
     *
     * @apiUse ForbiddenError
     * @apiUse ResourceError
     */
    public function store(Request $request, $question_id)
    {
        $this->authorize('store', DigitalizedQuestion::class);
        
        $user = Auth::guard('api')->user();

        $question = Question::with('digitalized_question')->findOrFail($question_id);

        if ($question->digitalized_question) {
            throw new ResourceException();
        }

        try {
            $digitalized_question = new DigitalizedQuestion();
            $digitalized_question->content = $request->content;
            $digitalized_question->question()->associate($question);
            $digitalized_question->user()->associate($user);
            $digitalized_question->save();
        } catch (QueryException $e) {
            throw new ResourceException();
        }

        return $digitalized_question;
    }

    /**
     * @api {patch} /questions/:id/digitalized_question 修改數位化問題
     * @apiGroup Questions
     *
     * @apiParam {String} content
     * @apiSuccess {Number} id
     *
     * @apiUse ResourceError
     */
    public function update(Request $request, $question_id)
    {
        $question = Question::findOrFail($question_id);
        $digitalized_question = $question->digitalized_question;

        if (!$digitalized_question) {
            throw new ResourceException();
        }
        $digitalized_question->content = $request->content;
        $digitalized_question->save();

        return $digitalized_question;
    }

    /**
     * @api {delete} /questions/:id/digitalized_question 刪除數位化問題
     * @apiGroup Questions
     *
     * @apiSuccess {String} status
     *
     * @apiUse ResourceError
     */
    public function destroy($question_id)
    {
        $question = Question::findOrFail($question_id);
        $digitalized_question = $question->digitalized_question;

        if (!$digitalized_question) {
            throw new ResourceException();
        }

        try {
            $digitalized_question->question()->dissociate();
            $digitalized_question->save();
        } catch (QueryException $e) {
            throw new ResourceException();
        }

        return ['status' => 'success'];
    }
}
