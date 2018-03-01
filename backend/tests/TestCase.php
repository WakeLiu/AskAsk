<?php

namespace Tests;

class TestCase extends \Illuminate\Foundation\Testing\TestCase
{
    /**
     * The base URL to use while testing the application.
     *
     * @var string
     */
    protected $baseUrl = 'http://localhost/api/v1';

    /**
     * Creates the application.
     *
     * @return \Illuminate\Foundation\Application
     */
    public function createApplication()
    {
        $app = require __DIR__.'/../bootstrap/app.php';

        $app->make(\Illuminate\Contracts\Console\Kernel::class)->bootstrap();

        return $app;
    }

    protected function seeJsonError($code = null)
    {
        if ($code) {
            $this->seeJsonStructure([
                'error',
            ])->assertResponseStatus($code);

            return $this;
        } else {
            return $this->seeJsonStructure([
                'error',
            ]);
        }
    }

    public function seeDecodedJson($callback)
    {
        $json = $this->decodeResponseJson();
        call_user_func($callback, $json);

        return $this;
    }

    public function jsonWithFiles($method, $uri, array $data, array $headers = [], $files = [])
    {
        $content = json_encode($data);

        $headers = array_merge([
            'CONTENT_LENGTH' => mb_strlen($content, '8bit'),
            'CONTENT_TYPE' => 'application/json',
            'Accept' => 'application/json',
        ], $headers);

        $this->call(
            $method, $uri, [], [], $files, $this->transformHeadersToServerVars($headers), $content
        );

        return $this;
    }

    protected function createImageFile()
    {
        $filename = str_random(10).'.jpg';
        @copy(base_path('tests/1908007.jpg'), base_path('tests/'.$filename));

        $imgFile = new \Illuminate\Http\UploadedFile(
            base_path('tests/'.$filename), $filename, 'image/jpeg', null, null, true
        );

        return $imgFile;
    }
}
