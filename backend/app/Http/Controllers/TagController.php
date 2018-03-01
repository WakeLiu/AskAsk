<?php

namespace App\Http\Controllers;

use App\Tag;
use Illuminate\Http\Request;
use App\Exceptions\ResourceException;
use Illuminate\Database\QueryException;

class TagController extends Controller
{
    public function index()
    {
        $tags = Tag::all();

        return $tags;
    }

    public function show($id)
    {
        $tag = Tag::findOrFail($id);

        return $tag;
    }

    public function showByName($name)
    {
        $tag = Tag::where('name', $name)->firstOrFail();

        return $tag;
    }

    public function store(Request $request)
    {
        $tag = new Tag();
        $tag->name = $request->name;
        $tag->type = $request->input('type', 'none');

        try {
            $tag->save();
        } catch (QueryException $e) {
            throw new ResourceException('duplicate name');
        }

        return $tag;
    }

    public function destroy($id)
    {
        $tag = Tag::findOrFail($id);

        $tag->delete();

        return ['status' => 'success'];
    }

    public function destroyByName($name)
    {
        $tag = Tag::where('name', $name)->firstOrFail();

        $tag->delete();

        return ['status' => 'success'];
    }
}
