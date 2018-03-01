<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class DigitalizedQuestion extends Model
{
    const MATH = 'math';

    public function user()
    {
        return $this->belongsTo('App\User');
    }

    public function question()
    {
        return $this->belongsTo('App\Question', 'question_id');
    }
}
