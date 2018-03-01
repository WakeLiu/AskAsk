<?php

namespace Tests;

use App\Tag;
use App\User;
use App\School;
use App\Student;

trait FakeTrait
{
    protected function fakeUser()
    {
        return factory(User::class)->create();
    }

    protected function fakeUserAdmin()
    {
        return factory(User::class, 'admin')->create();
    }

    protected function fakeStudent($school)
    {
        $student = factory(Student::class)->create()
            ->school()->associate($school);
        $student->save();

        $user = $this->fakeUser();
        $user->userable()->associate($student)->save();

        return $student;
    }

    protected function fakeSchool()
    {
        return factory(School::class)->create();
    }

    protected function fakeTag()
    {
        return factory(Tag::class)->create();
    }
}
