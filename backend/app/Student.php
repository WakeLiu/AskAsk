<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Student extends Model
{
    /**
     * The status code for Student.
     */
    const NEWLY_CREATED = 0;
    const ON_CHECKING = 1;
    const CHECKED = 2;

    public function user()
    {
        return $this->morphOne('App\User', 'userable');
    }

    public function school()
    {
        return $this->belongsTo('App\School');
    }

    public function photo()
    {
        return $this->belongsTo('App\Image');
    }

    public function scopeOfStatus($query, $status)
    {
        return $query->where('status', $status);
    }

    public function scopeChecked($query)
    {
        return $query->where('status', static::CHECKED);
    }
}
