<?php

namespace Tests;

use App\User;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class AuthTest extends TestCase
{
    use DatabaseTransactions;

    public function testLogin()
    {
        $user = factory(User::class)->create([
            'email' => 'email',
            'password' => bcrypt('a secret'),
        ]);

        $this->json('POST', '/login', ['email' => $user->email, 'password' => 'a secret'])
            ->seeJsonStructure([
                'token',
            ]);
    }

    public function testLoginFail()
    {
        $this->json('POST', '/login', ['email' => 'wrong', 'password' => 'wrong'])
            ->seeJsonError(401);
    }

    public function testLoginValidationFail()
    {
        $this->json('POST', '/login')
            ->seeJsonError(401);
    }

    public function testUnauthenticated()
    {
        $this->json('GET', '/questions')
            ->seeJsonError(401);
    }

    public function testRegister()
    {
        $device_uuid = str_random(10);
        $school = FakeFactory::fakeSchool();

        $password = null;
        $this->json('POST', '/register', [
            'device_uuid' => $device_uuid,
            'school_id' => $school->id,
            'grade' => '一年級',
            'name' => 'Mark',
            'gender' => '男',
        ])
            ->seeJson([
                'email' => $device_uuid . '@junior-master.com',
            ])
            ->seeJsonStructure([
                'student' => [
                    'school_id',
                    'grade',
                    'name',
                ],
                'password',
                'token',
            ])
            ->seeDecodedJson(function($json) use (&$password){
                $password = $json['password'];
            });

        // use the password to login
        $this->json('POST', '/login', [
            'email' => $device_uuid . '@junior-master.com',
            'password' => $password,
        ])
            ->seeJsonStructure([
                'token',
            ]);
    }
}
