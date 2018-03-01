<?php

namespace App\Http\Controllers;

use Auth;

class ApiController extends Controller
{
    /*
     * 使 authorize 能夠去拿到 api guard 的 user
     */
    public function authorize($ability, $arguments = [])
    {
        return $this->authorizeForUser(Auth::guard('api')->user(), $ability, $arguments);
    }
}
