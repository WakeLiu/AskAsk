<?php

namespace App\Http\Controllers\Me;

use Auth;
use App\Http\Controllers\Controller;

class QuestionController extends Controller
{
    public function index()
    {
        $user = Auth::guard('api')->user();

        return $user->questions()->paginate(15);
    }
}
