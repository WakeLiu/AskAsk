<?php

namespace App\Providers;

use App\School;
use App\Student;
use App\Question;
use App\Volunteer;
use App\DigitalizedQuestion;
use App\Policies\SchoolPolicy;
use App\Policies\StudentPolicy;
use App\Policies\QuestionPolicy;
use App\Policies\VolunteerPolicy;
use App\Policies\DigitalizedQuestionPolicy;
use Illuminate\Contracts\Auth\Access\Gate as GateContract;
use Illuminate\Foundation\Support\Providers\AuthServiceProvider as ServiceProvider;

class AuthServiceProvider extends ServiceProvider
{
    /**
     * The policy mappings for the application.
     *
     * @var array
     */
    protected $policies = [
        'App\Model' => 'App\Policies\ModelPolicy',
        School::class => SchoolPolicy::class,
        Question::class => QuestionPolicy::class,
        Student::class => StudentPolicy::class,
        Volunteer::class => VolunteerPolicy::class,
        DigitalizedQuestion::class => DigitalizedQuestionPolicy::class,
    ];

    /**
     * Register any application authentication / authorization services.
     *
     * @param \Illuminate\Contracts\Auth\Access\Gate $gate
     */
    public function boot(GateContract $gate)
    {
        $this->registerPolicies($gate);

        //
    }
}
