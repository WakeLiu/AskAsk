<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Volunteer extends Model
{
    public function user()
    {
        return $this->morphOne('App\User', 'userable');
    }

    public function photo()
    {
        return $this->belongsTo('App\Image');
    }
}
