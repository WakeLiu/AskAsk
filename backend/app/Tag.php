<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Tag extends Model
{
    public function scopeOfType($query, $type)
    {
        return $query->where('type', $type);
    }

    public function questions()
    {
        return $this->morphedByMany('App\Question', 'taggable');
    }
}
