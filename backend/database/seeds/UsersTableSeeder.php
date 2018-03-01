<?php

use Illuminate\Database\Seeder;

class UsersTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run()
    {
        $admin = factory(App\Admin::class)->create();

        $user = factory(App\User::class)->create([
            'email' => 'mark86092@gmail.com',
            'password' => bcrypt('admin'),
        ]);
        $user->userable()->associate($admin)->save();
    }
}
