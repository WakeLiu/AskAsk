<?php

namespace Tests;

use JWTAuth;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class SuTest extends TestCase
{
    use DatabaseTransactions;

    public function testAsUser()
    {
        $admin = FakeFactory::fakeAdmin();

        $user = FakeFactory::fakeUser();

        $token = null;

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/su/users/'.$user->id)
            ->seeJsonStructure([
                'token',
            ])
            ->seeDecodedJson(function ($json) use (&$token) {
                $token = $json['token'];
            });

        $this->assertEquals(
            $user->id,
            JWTAuth::setToken($token)->toUser()->id
        );
    }

    public function testAsUserByOther()
    {
        $actioner = FakeFactory::fakeUser();

        $user = FakeFactory::fakeUser();

        $this->actingAs($actioner, 'api')
            ->json('POST', '/su/users/'.$user->id)
            ->seeJsonError(403);
    }

    public function testAsStudent()
    {
        $admin = FakeFactory::fakeAdmin();

        $student = FakeFactory::fakeStudent();

        $token = null;

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/su/students/'.$student->id)
            ->seeJsonStructure([
                'token',
            ])
            ->seeDecodedJson(function ($json) use (&$token) {
                $token = $json['token'];
            });

        $this->assertEquals(
            $student->user->id,
            JWTAuth::setToken($token)->toUser()->id,
            'The student user id should match token user id'
        );
    }

    public function testAsVolunteer()
    {
        $admin = FakeFactory::fakeAdmin();

        $volunteer = FakeFactory::fakeVolunteer();

        $token = null;

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/su/volunteers/'.$volunteer->id)
            ->seeJsonStructure([
                'token',
            ])
            ->seeDecodedJson(function ($json) use (&$token) {
                $token = $json['token'];
            });

        $this->assertEquals(
            $volunteer->user->id,
            JWTAuth::setToken($token)->toUser()->id,
            'The volunteer user id should match token user id'
        );
    }
}
