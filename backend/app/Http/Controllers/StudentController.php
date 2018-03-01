<?php

namespace App\Http\Controllers;

use App\User;
use App\School;
use App\Student;
use Illuminate\Http\Request;
use App\Factories\UserFactory;
use App\Repositories\ImageUploadRepository;

class StudentController extends ApiController
{
    protected $userFactory;
    protected $image;

    public function __construct(UserFactory $userFactory, ImageUploadRepository $image)
    {
        $this->userFactory = $userFactory;
        $this->image = $image;
    }

    /**
     * @api {GET} /students List all students
     * @apiGroup Student
     *
     * @apiPermission Admin
     *
     * @apiParam {Number=1,0} [show_all=0]
     *
     * @apiSuccess {Number} per_page 15
     * @apiSuccess {Object[]} data Students
     * @apiSuccess {Number} data.id
     * @apiSuccess {Object} data.user
     * @apiSuccess {Object} data.school
     * @apiSuccess {Image} data.photo
     * @apiSuccess {String} data.photo.filename Image 網址
     *
     * @apiUse UnauthorizedError
     * @apiUse ForbiddenError
     */
    public function index(Request $request)
    {
        $this->authorize('index', Student::class);

        $query = Student::with([
            'user',
            'photo',
            'school',
        ]);

        if ($request->has('show_all') && $request->show_all == '1') {
            return $query->get(); 
        }

        return $query->paginate(15);
    }

    /**
     * @api {GET} /students/{:id} Show one student
     * @apiGroup Student
     *
     * @apiPermission Admin
     *
     * @apiSuccess {Number} id
     * @apiSuccess {String} gender
     * @apiSuccess {String} grade
     * @apiSuccess {String} name
     * @apiSuccess {String} nickname
     * @apiSuccess {Object} user
     * @apiSuccess {Object} school
     * @apiSuccess {Image} photo (can be `null`)
     * @apiSuccess {String} photo.filename Image 網址
     *
     * @apiUse UnauthorizedError
     * @apiUse NotFoundError
     * @apiUse ForbiddenError
     */
    public function show($id)
    {
        $student = Student::with([
            'user',
            'photo',
            'school',
        ])->findOrFail($id);

        $this->authorize('show', $student);

        return $student;
    }

    /**
     * @api {post} /students 新增學生
     * @apiGroup Student
     * @apiPermission Admin
     *
     * @apiParam {Number} school_id
     * @apiParam {String} name
     * @apiParam {String} grade
     * @apiParam {String} gender 性別：男、女
     * @apiParam {String} [nickname]
     * @apiParam {Number} [status]
     * @apiParam {File} [photo_file]
     *
     * @apiUse UnauthorizedError
     * @apiUse ForbiddenError
     * @apiUse ValidationError
     *
     * @apiSuccess {Number} id
     * @apiSuccess {Object} user
     * @apiSuccess {Image} photo
     * @apiSuccess {String} photo.filename Photo 網址
     */
    public function store(Request $request)
    {
        $this->authorize('store', Student::class);

        $this->validate($request, [
            'school_id' => 'required|exists:schools,id',
            'name' => 'required',
            'grade' => 'required',
            'gender' => 'required|in:男,女',
        ]);

        $student = new Student();

        $school = School::find($request->school_id);

        $student->school()->associate($school);
        $student->name = $request->input('name');
        if ($request->has('nickname')) {
            $student->nickname = $request->input('nickname');
        }
        $student->grade = $request->input('grade');
        $student->gender = $request->input('gender');

        if ($request->has('status')) {
            $student->status = $request->input('status');
        } else {
            $student->status = '0';
        }

        if ($request->hasFile('photo_file')) {
            $file = $request->file('photo_file');

            $image = $this->image->createImageFromUploadedFile($file);
            $image->save();

            $student->photo()->associate($image);
        }

        $student->save();

        $user = new User();
        $user->userable()->associate($student);
        $user->save();

        return $student->load(['user', 'photo']);
    }

    /**
     * @api {patch} /students/:student_id 更新資料
     * @apiGroup Student
     * @apiPermission Admin, Owner
     *
     * @apiParam {Number} [school_id]
     * @apiParam {String} [name]
     * @apiParam {String} [nickname]
     * @apiParam {String} [grade]
     * @apiParam {File} [photo_file]
     *
     * @apiUse UnauthorizedError
     * @apiUse NotFoundError
     * @apiUse ForbiddenError
     * @apiUse ValidationError
     *
     * @apiSuccess {Number} id
     * @apiSuccess {Image} photo
     * @apiSuccess {String} photo.filename Photo 網址
     */
    public function update(Request $request, $id)
    {
        $student = Student::findOrFail($id);

        $this->authorize('update', $student);

        $this->validate($request, [
            'school_id' => 'sometimes|exists:schools,id',
        ]);

        if ($request->has('school_id')) {
            $school = School::find($request->school_id);
            $student->school()->associate($school);
        }
        if ($request->has('name')) {
            $student->name = $request->input('name');
        }
        if ($request->has('nickname')) {
            $student->nickname = $request->input('nickname');
        }
        if ($request->has('grade')) {
            $student->grade = $request->input('grade');
        }

        if ($request->hasFile('photo_file')) {
            $file = $request->file('photo_file');

            $image = $this->image->createImageFromUploadedFile($file);
            $image->save();

            $student->photo()->associate($image);
        }

        $student->save();

        return $student->load(['user', 'photo', 'school']);
    }

    /**
     * @api {post} /students/:student_id/photo 上傳新照片
     * @apiGroup Student
     * @apiPermission Admin, Owner
     *
     * @apiParam {File} photo_file
     *
     * @apiUse UnauthorizedError
     * @apiUse NotFoundError
     * @apiUse ForbiddenError
     * @apiUse ValidationError
     *
     * @apiSuccess {Number} id
     * @apiSuccess {Image} photo
     * @apiSuccess {String} photo.filename Photo 網址
     */
    public function updatePhoto(Request $request, $id)
    {
        $student = Student::findOrFail($id);

        $this->authorize('update', $student);

        $this->validate($request, [
            'photo_file' => 'required|image',
        ]);

        $file = $request->file('photo_file');

        $image = $this->image->createImageFromUploadedFile($file);
        $image->save();

        $student->photo()->associate($image);
        $student->save();

        return $student->load('photo');
    }

    /**
     * @api {patch} /students/:student_id/status 更新狀態資料
     * @apiGroup Student
     * @apiPermission Admin
     *
     * @apiParam {Number} status
     *
     * @apiUse UnauthorizedError
     * @apiUse NotFoundError
     * @apiUse ForbiddenError
     * @apiUse ValidationError
     *
     * @apiSuccess {Number} id
     */
    public function updateStatus(Request $request, $id)
    {
        $student = Student::findOrFail($id);

        $this->authorize('update_status', $student);

        $this->validate($request, [
            'status' => 'in:0,1,2,3',
        ]);

        $student->status = $request->input('status');
        $student->save();

        return $student;
    }
}
