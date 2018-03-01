<?php

namespace Tests;

use App\Question;
use App\DigitalizedQuestion;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class DigitalizedQuestionTest extends TestCase
{
    use DatabaseTransactions;

    public function testIndex()
    {
        $admin = FakeFactory::fakeAdmin();

        factory(DigitalizedQuestion::class, 2)->create()->each(function ($q) {
            $question = FakeFactory::fakeQuestion();
            $q->question()->associate($question);
            $q->save();
        });
        factory(DigitalizedQuestion::class, 3)->create();

        $this->actingAs($admin->user, 'api')
            ->json('GET', '/digitalized_questions')
            ->seeJsonStructure([
                'data' => [
                    '*' => [
                        'id',
                        'user_id',
                        'user',
                        'content',
                        'question_id',
                        'question',
                    ],
                ],
            ])->seeDecodedJson(function ($json) {
                $this->assertCount(5, $json['data']);
            });
    }

    public function testStoreByAdmin()
    {
        $admin = FakeFactory::fakeAdmin();

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/digitalized_questions', ['content' => 'for test'])
            ->seeJson([
                'content' => 'for test',
                'user_id' => $admin->user->id,
            ]);
    }

    public function testStoreByVolunteer()
    {
        $volunteer = FakeFactory::fakeVolunteer();

        $this->actingAs($volunteer->user, 'api')
            ->json('POST', '/digitalized_questions', ['content' => 'for test'])
            ->seeJson([
                'content' => 'for test',
            ]);
    }

    public function testStoreByStudent()
    {
        $student = FakeFactory::fakeStudent();

        $this->actingAs($student->user, 'api')
            ->json('POST', '/digitalized_questions', ['content' => 'for test'])
            ->seeJsonError(403);
    }
}
