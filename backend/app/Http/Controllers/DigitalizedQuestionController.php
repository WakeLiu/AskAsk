<?php

namespace App\Http\Controllers;

use Auth;
use App\User;
use App\DigitalizedQuestion;
use Illuminate\Http\Request;
use App\Repositories\ImageUploadRepository;

class DigitalizedQuestionController extends ApiController
{
    public function index()
    {
        $digitalized_questions = DigitalizedQuestion::with(['user', 'question'])->paginate(15);

        return $digitalized_questions;
    }

    public function store(Request $request)
    {
        $this->authorize('store', DigitalizedQuestion::class);

        $user = Auth::guard('api')->user();

        $digitalized_question = new DigitalizedQuestion();
        $digitalized_question->content = $request->content;
        $digitalized_question->user()->associate($user);
        $digitalized_question->save();

        return $digitalized_question;
    }
}
