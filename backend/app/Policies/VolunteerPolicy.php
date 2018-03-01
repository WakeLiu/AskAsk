<?php

namespace App\Policies;

use App\User;
use App\Volunteer;
use Illuminate\Auth\Access\HandlesAuthorization;

/*
 * 志工資料權限表
 *
 * # Admin 具有所有權限
 *
 * - Student, Admin 可以檢視 (index)
 *
 * - Admin 可以新增 (store)
 */
class VolunteerPolicy
{
    use HandlesAuthorization;

    public function before($user, $ability)
    {
        if ($user->isAdmin()) {
            return true;
        }
    }

    public function index(User $user)
    {
        if ($user->isVolunteer()) {
            return false;
        }

        return true;
    }

    public function show(User $user)
    {
        if ($user->isVolunteer()) {
            return false;
        }

        return true;
    }

    public function store(User $user)
    {
        return false;
    }
}
