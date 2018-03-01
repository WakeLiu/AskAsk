<?php

namespace App\Http\Controllers;

use App\ChallengeQuestion;
use Illuminate\Http\Request;

class ChallengeQuestionController extends Controller
{
    public function index()
    {
        return ChallengeQuestion::paginate(15);
    }

    public function show($id)
    {
        $challenge_question = ChallengeQuestion::with(['user'])->findOrFail($id);

        return $challenge_question;
    }

    public function store(Request $request)
    {
        $this->validate($request, [
            'content' => 'required',
        ]);

        $user = Auth::guard('api')->user();

        $challenge_question = new ChallengeQuestion();
        $challenge_question->type = 'MATH';
        $challenge_question->content = $request->content;
        $challenge_question->user()->associate($user);

        $challenge_question->save();

        return $challenge_question;
    }

    public function update(Request $request, $id)
    {
        $this->validate($request, [
            'content' => 'required',
        ]);

        $challenge_question = ChallengeQuestion::findOrFail($id);
        $challenge_question->content = $request->content;

        $challenge_question->save();

        return $challenge_question;
    }

    public function destroy($id)
    {
        $challenge_question = ChallengeQuestion::findOrFail($id);

        $challenge_question->delete();

        return ['status' => 'success'];
    }
}
