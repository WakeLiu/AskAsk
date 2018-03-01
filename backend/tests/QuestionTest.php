<?php

namespace Tests;

use App\Tag;
use App\Answer;
use App\Question;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class QuestionTest extends TestCase
{
    use DatabaseTransactions;

    public function testQuestionStore()
    {
        $student = FakeFactory::fakeStudent();
        $admin = FakeFactory::fakeAdmin();

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/questions', ['content' => 'Mark Chen', 'user_id' => $student->user->id])
            ->seeJson([
                'user_id' => $student->user->id,
                'type' => Question::MATH,
                'content' => 'Mark Chen',
                'solved' => false,
            ]);

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/questions')
            ->seeJsonError(422);
    }

    public function testQuestionStoreByStudent()
    {
        $student = FakeFactory::fakeStudent();

        $this->actingAs($student->user, 'api')
            ->json('POST', '/questions', ['content' => 'Mark Chen'])
            ->seeJson([
                'user_id' => $student->user->id,
                'type' => Question::MATH,
                'content' => 'Mark Chen',
                'school_id' => $student->school->id,
                'grade' => $student->grade ? $student->grade : "",
                'solved' => false,
                'images' => [],
            ]);
    }

    public function testQuestionStoreByStudentWithImages()
    {
        $student = FakeFactory::fakeStudent();

        $filename = str_random(10).'.jpg';
        @copy(base_path('tests/1908007.jpg'), base_path('tests/'.$filename));

        $img = new \Illuminate\Http\UploadedFile(
            base_path('tests/'.$filename), $filename, 'image/jpeg', null, null, true
        );

        $this->actingAs($student->user, 'api')
            ->jsonWithFiles('POST', '/questions', ['content' => 'Mark Chen'], [], ['files' => [$img]])
            ->seeJson([
                'user_id' => $student->user->id,
                'type' => Question::MATH,
                'content' => 'Mark Chen',
                'school_id' => $student->school->id,
                'grade' => $student->grade ? $student->grade : "",
                'solved' => false,
            ])
            ->seeJsonStructure([
                'images',
            ]);
    }

    public function testQuestionUpdate()
    {
        $question = FakeFactory::fakeQuestion();

        $this->actingAs($question->user, 'api')
            ->json('PATCH', '/questions/'.$question->id, ['content' => 'Mark Chen'])
            ->seeJson([
                'content' => 'Mark Chen',
            ]);

        $this->actingAs($question->user, 'api')
            ->json('PATCH', '/questions/'.$question->id)
            ->seeJsonError(422);
    }

    public function testQuestionSolved()
    {
        $student = FakeFactory::fakeStudent();
        $question = FakeFactory::fakeQuestion($student);

        $this->actingAs($student->user, 'api')
            ->json('PATCH', '/questions/'.$question->id.'/solved', ['solved' => '1'])
            ->seeJson([
                'status' => 'success',
            ]);

        $this->actingAs($student->user, 'api')
            ->json('GET', '/questions/'.$question->id)
            ->seeJson([
                'solved' => 1,
            ]);

        $this->actingAs($student->user, 'api')
            ->json('PATCH', '/questions/'.$question->id.'/solved', ['solved' => '0'])
            ->seeJson([
                'status' => 'success',
            ]);

        $this->actingAs($student->user, 'api')
            ->json('GET', '/questions/'.$question->id)
            ->seeJson([
                'solved' => 0,
            ]);

        $this->actingAs($student->user, 'api')
            ->json('PATCH', '/questions/'.$question->id.'/solved', ['solved' => 'yes'])
            ->seeJsonError(422);
    }

    public function testShow()
    {
        $question = FakeFactory::fakeQuestion();

        $this->actingAs($question->user, 'api')
            ->json('GET', '/questions/'.$question->id)
            ->seeJson([
                'id' => $question->id,
                'user_id' => $question->user->id,
                'content' => $question->content,
                'solved' => $question->solved ? 1 : 0,
            ])->seeJsonStructure([
                'user',
                'user' => ['userable'],
                'answer',
                'images',
                'school',
                'tags',
                'digitalized_question',
            ]);
    }

    public function testAnswerStore()
    {
        $question = FakeFactory::fakeQuestion();

        $this->actingAs($question->user, 'api')
            ->json('POST', '/questions/'.$question->id.'/answer', ['content' => 'for test'])
            ->seeJson([
                'content' => 'for test',
            ]);

        // Can not insert Answer if exist	
        $this->actingAs($question->user, 'api')
            ->json('POST', '/questions/'.$question->id.'/answer', ['content' => 'for bug'])
            ->seeJsonError(409);
    }

    public function testAnswerUpdate()
    {
        $user = FakeFactory::fakeUser();
        $question = FakeFactory::fakeQuestion();

        // check can not update	if empty
        $this->actingAs($user, 'api')
            ->json('PATCH', '/questions/'.$question->id.'/answer', ['content' => 'for test'])
            ->seeJsonError(409);

        $answer = FakeFactory::fakeAnswer($user, $question);
        $this->actingAs($user, 'api')
            ->json('PATCH', '/questions/'.$question->id.'/answer', ['content' => $answer->content])
            ->seeJson([
                'user_id' => $user->id,
                'answerable_id' => $question->id,
                'answerable_type' => Question::class,
                'content' => $answer->content,
            ]);
    }

    public function testTagCanAttach()
    {
        $question = FakeFactory::fakeQuestion();

        $tag = factory(Tag::class)->create();

        $this->actingAs($question->user, 'api')
            ->json('POST', '/questions/'.$question->id.'/tags', ['id' => $tag->id])
            ->seeJson([
                'id' => $question->id,
            ])
            ->seeDecodedJson(function ($data) {
                $this->assertCount(1, $data['tags']);
            });
    }

    public function testTagCanDetach()
    {
        $question = FakeFactory::fakeQuestion();

        $tag = factory(Tag::class)->create();
        $question->tags()->attach($tag);

        $this->actingAs($question->user, 'api')
            ->json('DELETE', '/questions/'.$question->id.'/tags/'.$tag->id)
            ->seeJson([
                'id' => $question->id,
            ])
            ->seeDecodedJson(function ($data) {
                $this->assertCount(0, $data['tags']);
            });
    }
}
