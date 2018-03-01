<?php

namespace App\Http\Controllers;

use Auth;
use JWTAuth;
use App\User;
use App\Student;
use App\Volunteer;
use Illuminate\Auth\Access\AuthorizationException;

class SuController extends ApiController
{
    protected function getTokenFromUser($user)
    {
        return JWTAuth::fromUser($user);
    }

    public function asUser($user_id)
    {
        if (!Auth::guard('api')->user()->isAdmin()) {
            throw new AuthorizationException();
        }

        $user = User::findOrFail($user_id);

        return ['token' => $this->getTokenFromUser($user)];
    }

    public function asStudent($student_id)
    {
        if (!Auth::guard('api')->user()->isAdmin()) {
            throw new AuthorizationException();
        }

        $student = Student::findOrFail($student_id);

        return ['token' => $this->getTokenFromUser($student->user)];
    }

    public function asVolunteer($volunteer_id)
    {
        if (!Auth::guard('api')->user()->isAdmin()) {
            throw new AuthorizationException();
        }

        $volunteer = Volunteer::findOrFail($volunteer_id);

        return ['token' => $this->getTokenFromUser($volunteer->user)];
    }
}
