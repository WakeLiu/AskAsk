<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Answer extends Model
{
    public function user()
    {
        return $this->belongsTo('App\User');
    }

    public function answerable()
    {
        return $this->morphTo('answerable');
    }
}
