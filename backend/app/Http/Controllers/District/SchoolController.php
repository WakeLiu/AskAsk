<?php

namespace App\Http\Controllers\District;

use App\School;
use App\Http\Controllers\Controller;

class SchoolController extends Controller
{
    public function index($district)
    {
        return School::where('district', $district)->get();
    }
}
