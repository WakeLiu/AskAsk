<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Question extends Model
{
    const MATH = 'math';
    public function user()
    {
        return $this->belongsTo('App\User');
    }

    public function images()
    {
        return $this->morphMany('App\Image', 'imageable');
    }

    public function answer()
    {
        return $this->morphOne('App\Answer', 'answerable');
    }

    public function tags()
    {
        return $this->morphToMany('App\Tag', 'taggable');
    }

    public function school()
    {
        return $this->belongsTo('App\School');
    }

    public function digitalized_question()
    {
        return $this->hasOne('App\DigitalizedQuestion');
    }

    public function scopeInSchools($query, $school_string)
    {
        $school_id_array = explode(',', $school_string);

        return $query->whereIn('school_id', $school_id_array);
    }
}
