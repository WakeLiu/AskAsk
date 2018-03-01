<?php

namespace Tests;

use App\Admin;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class AdminTest extends TestCase
{
    use FakeTrait;
    use DatabaseTransactions;

    public function testAdminStore()
    {
        $user = $this->fakeUser();

        $this->actingAs($user, 'api')
            ->json('POST', '/users/admins', ['email' => 'test', 'password' => str_random(10)])
            ->seeJsonStructure([
                'user',
            ]);
    }

    protected function fakeAdmin()
    {
        $admin = factory(Admin::class)->create();

        $user = $this->fakeUser();
        $user->userable()->associate($admin)->save();

        return $admin;
    }
}
