<?php

namespace App\Http\Controllers\Question;

use App\Tag;
use App\Question;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;

class TagController extends Controller
{
    public function store(Request $request, $question_id)
    {
        $this->validate($request, [
            'id' => 'required',
        ]);

        $question = Question::findOrFail($question_id);

        $tag = Tag::findOrFail($request->id);

        $question->tags()->attach($tag->id);

        return $question->load('tags');
    }

    public function destroy(Request $request, $question_id, $tag_id)
    {
        $question = Question::findOrFail($question_id);
        $tag = Tag::findOrFail($tag_id);

        $question->tags()->detach($tag_id);

        return $question->load('tags');
    }
}
