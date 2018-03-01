<?php

namespace App\Http\Controllers\Auth;

use JWTAuth;
use App\User;
use App\School;
use App\Student;
use Illuminate\Http\Request;
use App\Factories\UserFactory;
use App\Http\Controllers\Controller;
use Tymon\JWTAuth\Exceptions\JWTException;
use App\Repositories\ImageUploadRepository;

/**
 * @api {POST} /login 登入
 * @apiGroup Auth
 *
 * @apiParam {string} email
 * @apiParam {string} password
 * 
 * @apiSuccess {String} token
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 * {
 *   "token": "xxxxx"
 * }
 * @apiError Unauthorized `401` 帳號密碼錯誤
 */

class JWTAuthController extends Controller
{
    protected $userFactory;
    protected $image;

    public function __construct(UserFactory $userFactory, ImageUploadRepository $image)
    {
        $this->userFactory = $userFactory;
        $this->image = $image;
    }

    public function emailLogin(Request $request)
    {
        $credentials = $request->only('email', 'password');

        try {
            if (!$token = JWTAuth::attempt($credentials)) {
                throw new \App\Exceptions\UnauthorizedHttpException();
            }
        } catch (JWTException $e) {
            return response()->json(['error' => 'could_not_create_token'], 500);
        }

        return response()->json(['token' => $token]);
    }

    /**
     * @api {post} /register 註冊
     * @apiGroup Register
     *
     * @apiParam {String} device_uuid
     * @apiParam {Number} school_id
     * @apiParam {String} grade
     * @apiParam {String} name
     * @apiParam {String} gender 性別：男、女
     *
     * @apiSuccess {String} token token, can be used as auth token
     * @apiSuccess {String} email
     * @apiSuccess {String} password
     * @apiSuccess {Object} student
     */
    public function register(Request $request)
    {
        $this->validate($request, [
            'device_uuid' => 'required',
            'school_id' => 'required|exists:schools,id',
            'grade' => 'required',
            'name' => 'required',
            'gender' => 'required|in:男,女',
        ]);

        $student = new Student();

        $school = School::find($request->school_id);

        $student->school()->associate($school);
        $student->grade = $request->input('grade');
        $student->name = $request->input('name');
        $student->gender = $request->input('gender');
        $student->status = '0';
        $student->save();

        // TODO create a table for store ID image, device, and student_id

        $email = $request->input('device_uuid').'@junior-master.com';
        $password = str_random(10);

        $user = new User();
        $user->userable()->associate($student);
        $user->email = $email;
        $user->password = bcrypt($password);
        try {
            $user->save();
        } catch (\Exception $e) {
            // TODO when user attempt to use the same device uuid to register
            // NEED Discussion
            return response()->json([], 500);
        }

        $token = JWTAuth::attempt(['email' => $email, 'password' => $password]);

        return [
            'student' => $student,
            'email' => $email,
            'password' => $password,
            'token' => $token,
        ];
    }
}
