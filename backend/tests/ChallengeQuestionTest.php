<?php

namespace Tests;

use App\ChallengeQuestion;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class ChallengeQuestionTest extends TestCase
{
    use DatabaseTransactions;
    use FakeTrait;

    public function testIndex()
    {
        $user = $this->fakeUser();
        $challenge_questions = factory(ChallengeQuestion::class, 2)->create()->each(function ($q) use ($user) {
            $q->user()->associate($user);
            $q->save();
        });

        $this->actingAs($user, 'api')
            ->json('GET', '/challenge_questions')
            ->seeJsonStructure([
                'data' => [
                    '*' => [
                        'id', 'user_id', 'content',
                    ],
                ],
            ]);
    }

    public function testStore()
    {
        $this->markTestIncomplete();
    }

    public function testUpdate()
    {
        $this->markTestIncomplete();
    }

    public function testShow()
    {
        $this->markTestIncomplete();
    }

    public function testDestroy()
    {
        $this->markTestIncomplete();
    }
}
