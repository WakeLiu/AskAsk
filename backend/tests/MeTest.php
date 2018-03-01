<?php

namespace Tests;

use App\Question;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class MeTest extends TestCase
{
    use FakeTrait;
    use DatabaseTransactions;

    public function testShow()
    {
        $student = FakeFactory::fakeStudent();

        $this->actingAs($student->user, 'api')
            ->json('GET', '/me')
            ->seeJsonStructure([
                'id',
                'name',
                'nickname',
                'gender',
                'photo',
                'school',
                'school' => [
                    'name',
                ]
            ]);
    }

    public function testQuestions()
    {
        $user = $this->fakeUser();
        $questions = factory(Question::class, 3)->make()
            ->each(function ($q) use ($user) {
                $q->user()->associate($user)->save();
            });

        $response = $this->actingAs($user, 'api')
            ->json('GET', '/me/questions')
            ->decodeResponseJson();

        $this->assertCount(3, $response['data']);
    }
}
