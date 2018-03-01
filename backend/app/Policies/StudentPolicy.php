<?php

namespace App\Policies;

use App\User;
use App\Student;
use Illuminate\Auth\Access\HandlesAuthorization;

class StudentPolicy
{
    use HandlesAuthorization;

    /**
     * Create a new policy instance.
     */
    public function __construct()
    {
        //
    }

    public function before($user, $ability)
    {
        if ($user->isAdmin()) {
            return true;
        }
    }

    public function index(User $user)
    {
        return false;
    }

    public function show(User $user, Student $student)
    {
        return false;
    }

    public function store(User $user)
    {
        return false;
    }

    public function update(User $user, Student $student)
    {
        if ($user->isStudent()) {
            return $user->userable->id === $student->id;
        }

        return false;
    }

    public function update_status(User $user)
    {
        return false;
    }
}
