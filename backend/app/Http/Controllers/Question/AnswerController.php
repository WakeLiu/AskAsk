<?php

namespace App\Http\Controllers\Question;

use Auth;
use App\Answer;
use App\Question;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Exceptions\ResourceException;
use Illuminate\Database\QueryException;

class AnswerController extends Controller
{
    public function store(Request $request, $question_id)
    {
        $user = Auth::guard('api')->user();
        $question = Question::findOrFail($question_id);

        try {
            $answer = new Answer();
            $answer->content = $request->content;
            $answer->user_id = $user->id;
            $answer->answerable()->associate($question);
            $answer->save();

            return $answer;
        } catch (QueryException $e) {
            # TODO not this exception
            throw new ResourceException();
        }
    }

    public function update(Request $request, $question_id)
    {
        $question = Question::findOrFail($question_id);
        $answer = $question->answer;

        if (!$answer) {
            # TODO not this exception
            throw new ResourceException();
        }
        $answer->content = $request->content;
        $answer->save();

        return $answer;
    }

    public function destroy($question_id)
    {
    }
}
