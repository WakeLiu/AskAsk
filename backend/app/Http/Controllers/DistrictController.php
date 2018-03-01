<?php

namespace App\Http\Controllers;

use DB;

class DistrictController extends Controller
{
    public function index()
    {
        $districts = DB::table('schools')->select('district')->distinct('district')->get();

        $districts = array_map(function ($n) {
            return $n->district;
        }, $districts);

        return $districts;
    }
}
