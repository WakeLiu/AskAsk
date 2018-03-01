<?php

namespace App\Http\Controllers;

use App\User;
use App\Admin;
use Illuminate\Http\Request;
use App\Factories\UserFactory;

class AdminController extends Controller
{
    protected $userFactory;
    public function __construct(UserFactory $userFactory)
    {
        $this->userFactory = $userFactory;
    }

    public function store(Request $request)
    {
        $this->validate($request, [
            'email' => 'required',
            'password' => 'required',
        ]);
        $admin = new Admin();
        $admin->save();

        $user = new User();
        $user->email = $request->email;
        $user->password = bcrypt($request->password);
        $user->userable()->associate($admin);
        $user->save();

        return $admin->load('user');
    }
}
