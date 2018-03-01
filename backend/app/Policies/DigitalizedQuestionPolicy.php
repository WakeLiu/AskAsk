<?php

namespace App\Policies;

use App\User;
use App\Question;
use Illuminate\Auth\Access\HandlesAuthorization;

class DigitalizedQuestionPolicy
{
    use HandlesAuthorization;

    public function before($user, $ability)
    {
        if ($user->isAdmin()) {
            return true;
        }
    }

    public function store(User $user)
    {
        return $user->isVolunteer();
    }

    public function update(User $user, DigitalizedQuestion $digitalized_question)
    {
        return $user->isVolunteer();
    }
}
