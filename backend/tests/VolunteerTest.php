<?php

namespace Tests;

use App\User;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class VolunteerTest extends TestCase
{
    use DatabaseTransactions;

    public function testIndexByAdmin()
    {
        FakeFactory::fakeVolunteer();
        FakeFactory::fakeVolunteer();

        $admin = FakeFactory::fakeAdmin();

        $this->actingAs($admin->user, 'api')
            ->json('GET', '/volunteers')
            ->seeJsonStructure([
                '*' => [
                    'user',
                    'profile',
                    'photo',
                ],
            ]);
    }

    public function testIndexByVolunteer()
    {
        $volunteer = FakeFactory::fakeVolunteer();

        $this->actingAs($volunteer->user, 'api')
            ->json('GET', '/volunteers')
            ->seeJsonError(403);
    }

    public function testIndexByStudent()
    {
        FakeFactory::fakeVolunteer();
        FakeFactory::fakeVolunteer();

        $student = FakeFactory::fakeStudent();

        $this->actingAs($student->user, 'api')
            ->json('GET', '/volunteers')
            ->seeJsonStructure([
                'data' => [
                    '*' => [
                        'id',
                        'name',
                        'gender',
                    ],
                ],
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertArrayNotHasKey('profile', $json['data'][0]);
            });
    }

    public function testShowByAdmin()
    {
        $volunteer = FakeFactory::fakeVolunteer();

        $admin = FakeFactory::fakeAdmin();

        $this->actingAs($admin->user, 'api')
            ->json('GET', '/volunteers/'.$volunteer->id)
            ->seeJsonStructure([
                'user',
                'profile',
                'photo',
            ]);
    }

    public function testShowByVolunteer()
    {
        $volunteer = FakeFactory::fakeVolunteer();

        $this->actingAs($volunteer->user, 'api')
            ->json('GET', '/volunteers/'.$volunteer->id)
            ->seeJsonError(403);
    }

    public function testShowByStudent()
    {
        $volunteer = FakeFactory::fakeVolunteer();

        $student = FakeFactory::fakeStudent();

        $this->actingAs($student->user, 'api')
            ->json('GET', '/volunteers/'.$volunteer->id)
            ->seeJsonStructure([
                'id',
                'name',
                'gender',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertArrayNotHasKey('profile', $json);
            });
    }


    public function testStoreByVolunteer()
    {
        $volunteer = FakeFactory::fakeVolunteer();

        $this->actingAs($volunteer->user, 'api')
            ->json('POST', '/volunteers', ['name' => 'Mark Chen', 'email' => 'test', 'password' => str_random(10)])
            ->seeJsonError(403);
    }

    public function testStore()
    {
        $admin = FakeFactory::fakeAdmin();

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/volunteers', ['name' => 'Mark Chen', 'email' => 'test', 'password' => str_random(10)])
            ->seeJson([
                'name' => 'Mark Chen',
            ])
            ->seeJsonStructure([
                'user' => [
                    'id',
                ],
                'photo',
            ]);
    }

    // Store API with photo
    public function testStoreWithPhoto()
    {
        $admin = FakeFactory::fakeAdmin();

        $imgFile = $this->createImageFile();

        $this->actingAs($admin->user, 'api')
            ->jsonWithFiles('POST', '/volunteers', [
                'name' => 'Mark Chen',
                'email' => 'test',
                'password' => str_random(10),
            ], [], ['photo_file' => $imgFile])
            ->seeJson([
                'name' => 'Mark Chen',
            ])
            ->seeJsonStructure([
                'user',
                'photo',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertNotNull($json['photo']);
            });
    }

    public function testUpdate()
    {
        $admin = FakeFactory::fakeAdmin();
        $volunteer = FakeFactory::fakeVolunteer();

        $this->actingAs($admin->user, 'api')
            ->json('PATCH', '/volunteers/'.$volunteer->id, [
                'name' => 'Mark',
                'profile' => 'Two',
            ])
            ->seeJson([
                'id' => $volunteer->id,
                'name' => 'Mark',
                'profile' => 'Two',
            ])
            ->seeJsonStructure([
                'user',
                'photo',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertNull($json['photo']);
            });
    }

    public function testUpdatePhoto()
    {
        $admin = FakeFactory::fakeAdmin();
        $volunteer = FakeFactory::fakeVolunteer();

        $imgFile = $this->createImageFile();

        $this->actingAs($admin->user, 'api')
            ->jsonWithFiles('PATCH', '/volunteers/'.$volunteer->id.'/photo', [], [], ['photo_file' => $imgFile])
            ->seeJson([
                'id' => $volunteer->id,
            ])
            ->seeJsonStructure([
                'user',
                'photo',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertNotNull($json['photo']);
            });
    }
}
