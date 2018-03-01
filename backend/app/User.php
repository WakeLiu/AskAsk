<?php

namespace App;

use Tymon\JWTAuth\Contracts\JWTSubject;
use Illuminate\Foundation\Auth\User as Authenticatable;

class User extends Authenticatable implements JWTSubject
{
    /**
     * Get the questions for the user.
     */
    public function questions()
    {
        return $this->hasMany('App\Question');
    }

    public function userable()
    {
        return $this->morphTo('userable');
    }

    public function isStudent()
    {
        return $this->userable_type === Student::class;
    }

    public function isVolunteer()
    {
        return $this->userable_type === Volunteer::class;
    }

    public function isAdmin()
    {
        return $this->userable_type === Admin::class;
    }

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'name', 'email', 'password',
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [
        'password', 'remember_token',
    ];

    public function getJWTIdentifier()
    {
        return $this->getKey();
    }

    public function getJWTCustomClaims()
    {
        return [];
    }
}
