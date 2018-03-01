<?php

namespace App\Http\Controllers;

use Auth;

class MeController extends Controller
{
    /**
     * @api {get} /me 列出所有 Questions
     * @apiGroup Me
     *
     * @apiPermission Student
     *
     * @apiUse UnauthorizedError
     * @apiUse ForbiddenError
     */
    public function show()
    {
        $user = Auth::guard('api')->user();
        if ($user->isStudent()) {
            return $this->showStudent($user);
        } else {
            throw new Error('');
        }
    }

    protected function showStudent($user)
    {
        $student = $user->userable;
        $student->load(['school', 'photo']);

        return $student;
    }
}
