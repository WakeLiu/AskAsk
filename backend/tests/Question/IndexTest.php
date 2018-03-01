<?php

namespace Tests\Question;

use App\Question;
use Tests\TestCase;
use Tests\FakeFactory;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class IndexTest extends TestCase
{
    use DatabaseTransactions;

    public function testIndex()
    {
        $student = FakeFactory::fakeStudent();

        $student1 = FakeFactory::fakeStudent();
        $student2 = FakeFactory::fakeStudent();

        factory(Question::class, 2)->create()->each(function ($q) use ($student1) {
            $q->user()->associate($student1->user);
            $q->school()->associate($student1->school);
            $q->save();
        });

        factory(Question::class, 2)->create()->each(function ($q) use ($student2) {
            $q->user()->associate($student2->user);
            $q->school()->associate($student2->school);
            $q->save();
        });

        $this->actingAs($student->user, 'api')
            ->json('GET', '/questions')
            ->seeJsonStructure([
                'data' => [
                    '*' => [
                        'id',
                        'content',
                        'solved',
                        'school' => [
                            'name',
                        ],
                        'user' => [
                            'userable' => [
                                'name',
                            ],
                        ],
                        'digitalized_question',
                    ],
                ],
            ])
            ->seeJsonStructure([
                'data' => [
                    '*' => [
                        'images',
                        'tags',
                    ],
                ],
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(4, $json['data']);
            });

        $this->actingAs($student->user, 'api')
            ->json('GET', '/questions', ['school_id' => $student1->school->id])
            ->seeJson([
                'total' => 2,
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(2, $json['data']);
            });

        $school_ids = [
            $student1->school->id,
            $student2->school->id,
        ];
        $this->actingAs($student->user, 'api')
            ->json('GET', '/questions', ['school_id' => implode(',', $school_ids)])
            ->seeJson([
                'total' => 4,
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(4, $json['data']);
            });
    }

    public function testIndexWithSolved()
    {
        $student = FakeFactory::fakeStudent();

        factory(Question::class, 2)->create(['solved' => true]);
        factory(Question::class, 1)->create(['solved' => false]);

        $this->actingAs($student->user, 'api')
            ->json('GET', '/questions', ['solved' => '1'])
            ->seeJson([
                'total' => 2,
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(2, $json['data']);
            });

        $this->actingAs($student->user, 'api')
            ->json('GET', '/questions', ['solved' => '0'])
            ->seeJson([
                'total' => 1,
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(1, $json['data']);
            });
    }

    public function testIndexWithGradeForVolunteer()
    {
        $volunteer = FakeFactory::fakeVolunteer();

        factory(Question::class, 2)->create(['grade' => '一年級']);
        factory(Question::class, 3)->create(['grade' => '二年級']);

        $this->actingAs($volunteer->user, 'api')
            ->json('GET', '/questions', ['grade' => '一年級'])
            ->seeJson([
                'total' => 2,
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(2, $json['data']);
            });

        $this->actingAs($volunteer->user, 'api')
            ->json('GET', '/questions', ['grade' => '三年級'])
            ->seeJson([
                'total' => 0,
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(0, $json['data']);
            });
    }

    public function testWithDigitalizedQuestionForVolunteer()
    {
        $volunteer = FakeFactory::fakeVolunteer();

        FakeFactory::fakeQuestionWithDigitalizedQuestion();
        FakeFactory::fakeQuestionWithDigitalizedQuestion();
        FakeFactory::fakeQuestion();

        $this->actingAs($volunteer->user, 'api')
            ->json('GET', '/questions', ['has_digitalized_question' => '1'])
            ->seeJson([
                'total' => 2,
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(2, $json['data']);
            });

        $this->actingAs($volunteer->user, 'api')
            ->json('GET', '/questions', ['has_digitalized_question' => '0'])
            ->seeJson([
                'total' => 1,
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(1, $json['data']);
            });
    }

    public function testIndexWithTag()
    {
        // TODO
        $this->markTestIncomplete();
    }

    public function testIndexWithSort()
    {
        // TODO
        $this->markTestIncomplete();
    }

    public function testIndexWithMine()
    {
        $student1 = FakeFactory::fakeStudent();
        $student2 = FakeFactory::fakeStudent();

        factory(Question::class, 2)->create()->each(function ($q) use ($student1) {
            $q->user()->associate($student1->user);
            $q->school()->associate($student1->school);
            $q->save();
        });

        factory(Question::class, 3)->create()->each(function ($q) use ($student2) {
            $q->user()->associate($student2->user);
            $q->school()->associate($student2->school);
            $q->save();
        });

        $this->actingAs($student1->user, 'api')
            ->json('GET', '/questions', ['mine' => '1'])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(2, $json['data']);
            });

        $this->actingAs($student2->user, 'api')
            ->json('GET', '/questions', ['mine' => '1'])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(3, $json['data']);
            });
    }
}
