<?php

namespace Tests;

use App\User;
use App\Admin;
use App\Answer;
use App\School;
use App\Student;
use App\Question;
use App\Volunteer;
use App\DigitalizedQuestion;

class FakeFactory
{
    public static function fakeAdmin()
    {
        $admin = factory(Admin::class)->create();

        $user = static::fakeUser();
        $user->userable()->associate($admin)->save();

        return $admin;
    }

    public static function fakeAnswer($user, $question)
    {
        $answer = factory(Answer::class)->create()
            ->user()->associate($user)
            ->answerable()->associate($question);
        $answer->save();

        return $answer;
    }

    public static function fakeSchool()
    {
        return factory(School::class)->create();
    }

    public static function fakeStudent(School $school = null)
    {
        if (is_null($school)) {
            $school = static::fakeSchool();
        }

        $student = factory(Student::class)->create();
        $student->school()->associate($school);
        $student->save();

        $user = static::fakeUser();
        $user->userable()->associate($student)->save();

        return $student;
    }

    public static function fakeUser()
    {
        return factory(User::class)->create();
    }

    public static function fakeDigitalizedQuestion(Question $question = null)
    {
        // TODO: user
        $digitalized_question = factory(DigitalizedQuestion::class)->create();

        if (is_null($question)) {
            $question = static::fakeQuestion();
        }

        $digitalized_question->question()->associate($question);
        $digitalized_question->save();

        return $digitalized_question;
    }

    public static function fakeQuestionWithDigitalizedQuestion(
        Student $student = null, DigitalizedQuestion $digitalized_question = null
    ) {
        $question = static::fakeQuestion($student);

        if (is_null($digitalized_question)) {
            $digitalized_question = static::fakeDigitalizedQuestion($question);
        } else {
            $digitalized_question->question()->associate($question);
            $digitalized_question->save();
        }

        return $question;
    }

    public static function fakeQuestion(Student $student = null)
    {
        if (is_null($student)) {
            $student = static::fakeStudent();
        }

        $question = factory(Question::class)->create();
        $question->user()->associate($student->user);
        $question->school()->associate($student->school);
        $question->save();

        return $question;
    }

    public static function fakeVolunteer()
    {
        $volunteer = factory(Volunteer::class)->create();

        $user = static::fakeUser();
        $user->userable()->associate($volunteer)->save();

        return $volunteer;
    }
}
