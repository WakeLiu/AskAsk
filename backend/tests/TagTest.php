<?php

namespace Tests;

use App\Tag;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class TagTest extends TestCase
{
    use DatabaseTransactions;
    use FakeTrait;

    public function testIndex()
    {
        $user = $this->fakeUser();
        $tags = factory(Tag::class, 3)->create();

        $this->actingAs($user, 'api')
            ->json('GET', '/tags')
            ->seeJsonStructure([
                '*' => [
                    'id',
                ],
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertCount(3, $json);
            });
    }

    public function testTagStore()
    {
        $user = $this->fakeUser();

        $this->actingAs($user, 'api')
            ->json('POST', '/tags', ['name' => '高一數學'])
            ->seeJson([
                'name' => '高一數學',
                'type' => 'none',
            ]);
    }

    public function testTagCannotStoreDuplicateName()
    {
        $user = $this->fakeUser();
        $tag = factory(Tag::class)->create(['name' => '高一數學']);

        $this->actingAs($user, 'api')
            ->json('POST', '/tags', ['name' => '高一數學'])
            ->seeJsonError(409);
    }

    public function testTagShow()
    {
        $user = $this->fakeUser();
        $tag = $this->fakeTag();

        $this->actingAs($user, 'api')
            ->json('GET', '/tags/'.$tag->id)
            ->seeJson([
                'id' => $tag->id,
                'name' => $tag->name,
                'type' => $tag->type,
            ]);
    }

    public function testTagShowByName()
    {
        $user = $this->fakeUser();
        $tag = factory(Tag::class)->create(['name' => '高一數學']);

        $this->actingAs($user, 'api')
            ->json('GET', '/tags/'.$tag->name)
            ->seeJson([
                'id' => $tag->id,
                'name' => $tag->name,
                'type' => $tag->type,
            ]);
    }

    public function testTagDestroy()
    {
        $user = $this->fakeUser();
        $tag = factory(Tag::class)->create(['name' => '高一數學']);

        $this->actingAs($user, 'api')
            ->json('DELETE', '/tags/'.$tag->name)
            ->seeJson([
                'status' => 'success',
            ]);

        $this->actingAs($user, 'api')
            ->json('DELETE', '/tags/'.$tag->id)
            ->seeJsonError(404);
    }
}
