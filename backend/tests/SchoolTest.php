<?php

namespace tests;

use App\School;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class SchoolTest extends TestCase
{
    use FakeTrait;
    use DatabaseTransactions;

    public function testSchoolStore()
    {
        $user = $this->fakeUserAdmin();

        $this->actingAs($user, 'api')
            ->json('POST', '/schools', ['edu_id' => 'random', 'fullname' => 'hello'])
            ->seeJson([
                'edu_id' => 'random',
                'fullname' => 'hello',
            ])
            ->seeJsonStructure([
                'id',
            ]);

        // Test required fields
        $this->actingAs($user, 'api')
            ->json('POST', '/schools')
            ->seeJsonError(422);

        // Test duplicate keys
        $this->actingAs($user, 'api')
            ->json('POST', '/schools', ['edu_id' => 'random', 'fullname' => 'hello'])
            ->seeJsonError(409);
    }

    public function testSchoolStorePolicy()
    {
        $user = $this->fakeUser();

        $this->actingAs($user, 'api')
            ->json('POST', '/schools', ['edu_id' => 'random', 'fullname' => 'hello'])
            ->seeJsonError(403);
    }

    public function testSchoolIndex()
    {
        $this->fakeSchool();
        $this->fakeSchool();
        $user = $this->fakeUserAdmin();

        $this->actingAs($user, 'api')
            ->json('GET', '/schools')
            ->seeJsonStructure([
                'total',
                'data' => [
                    '*' => [
                        'id',
                        'edu_id',
                        'fullname',
                        'name',
                        'district',
                    ]
                ],
            ]);
    }

    public function testSchoolShow()
    {
        $this->markTestIncomplete();
    }

    public function testSchoolUpdate()
    {
        $this->markTestIncomplete();
    }

    public function testSchoolDestroy()
    {
        $this->markTestIncomplete();
    }

    public function testDistrictIndex()
    {
        $user = $this->fakeUserAdmin();
        factory(School::class)->create(['district' => 'mark']);
        factory(School::class)->create(['district' => 'chen']);

        $this->actingAs($user, 'api')
            ->json('GET', '/districts')
            ->seeDecodedJson(function ($json) {
                $this->assertCount(2, $json);
            });
    }

    public function testSchoolIndexWithDistrict()
    {
        $user = $this->fakeUserAdmin();
        factory(School::class)->create(['district' => 'mark']);
        factory(School::class)->create(['district' => 'chen']);

        $this->actingAs($user, 'api')
            ->json('GET', '/districts/mark/schools')
            ->seeDecodedJson(function ($json) {
                $this->assertCount(1, $json);
            })
            ->seeJsonStructure([
                '*' => [
                    'id',
                    'edu_id',
                    'fullname',
                    'name',
                    'district',
                ],
            ]);
    }
}
