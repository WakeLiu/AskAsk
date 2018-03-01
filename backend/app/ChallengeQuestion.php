<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class ChallengeQuestion extends Model
{
    public function user()
    {
        return $this->belongsTo('App\User');
    }
}
