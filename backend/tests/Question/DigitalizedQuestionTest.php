<?php

namespace Tests\Question;

use Tests\TestCase;
use Tests\FakeFactory;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class DigitalizedQuestionTest extends TestCase
{
    use DatabaseTransactions;

    public function testStoreByAdmin()
    {
        $admin = FakeFactory::fakeAdmin();

        $question = FakeFactory::fakeQuestion();

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/questions/'.$question->id.'/digitalized_question', ['content' => 'for test'])
            ->seeJson([
                'content' => 'for test',
                'user_id' => $admin->user->id,
            ]);

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/questions/'.$question->id.'/digitalized_question', ['content' => 'for bug'])
            ->seeJsonError(409);
    }

    public function testStoreByVolunteer()
    {
        $volunteer = FakeFactory::fakeVolunteer();

        $question = FakeFactory::fakeQuestion();

        $this->actingAs($volunteer->user, 'api')
            ->json('POST', '/questions/'.$question->id.'/digitalized_question', ['content' => 'for test'])
            ->seeJson([
                'content' => 'for test',
            ]);
    }

    public function testStoreByStudent()
    {
        $student = FakeFactory::fakeStudent();

        $question = FakeFactory::fakeQuestion();

        $this->actingAs($student->user, 'api')
            ->json('POST', '/questions/'.$question->id.'/digitalized_question', ['content' => 'for test'])
            ->seeJsonError(403);
    }

    public function testUpdate()
    {
        $admin = FakeFactory::fakeAdmin();

        $question = FakeFactory::fakeQuestionWithDigitalizedQuestion();

        $this->actingAs($admin->user, 'api')
            ->json('PATCH', '/questions/'.$question->id.'/digitalized_question', ['content' => 'for test'])
            ->seeJson([
                'content' => 'for test',
            ]);
    }

    public function testUpdateFail()
    {
        $admin = FakeFactory::fakeAdmin();

        $question = FakeFactory::fakeQuestion();

        $this->actingAs($admin->user, 'api')
            ->json('PATCH', '/questions/'.$question->id.'/digitalized_question', ['content' => 'for bug'])
            ->seeJsonError(409);
    }

    public function testDestroy()
    {
        $this->markTestIncomplete();
    }
}
