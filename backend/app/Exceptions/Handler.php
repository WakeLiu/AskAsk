<?php

namespace App\Exceptions;

use Exception;
use Illuminate\Http\JsonResponse;
use Illuminate\Validation\ValidationException;
use Illuminate\Auth\Access\AuthorizationException;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Symfony\Component\HttpKernel\Exception\HttpException;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Illuminate\Foundation\Exceptions\Handler as ExceptionHandler;

class Handler extends ExceptionHandler
{
    /**
     * A list of the exception types that should not be reported.
     *
     * @var array
     */
    protected $dontReport = [
        AuthorizationException::class,
        ResourceException::class,
        HttpException::class,
        ModelNotFoundException::class,
        ValidationException::class,
        UnauthorizedHttpException::class,
    ];

    /**
     * Report or log an exception.
     *
     * This is a great spot to send exceptions to Sentry, Bugsnag, etc.
     *
     * @param \Exception $e
     */
    public function report(Exception $e)
    {
        parent::report($e);
    }

    /**
     * Render an exception into an HTTP response.
     *
     * @param \Illuminate\Http\Request $request
     * @param \Exception               $e
     *
     * @return \Illuminate\Http\Response
     */
    public function render($request, Exception $e)
    {
        if ($request->ajax() || $request->wantsJson()) {
            if ($e instanceof NotFoundHttpException) {
                return $this->prepareErrorResponse('Not Found', 'NotFoundHttpException', 404);
            }
            if ($e instanceof ModelNotFoundException) {
                return $this->prepareErrorResponse('Not Found', 'NotFoundHttpException', 404);
            }
            if ($e instanceof AuthorizationException) {
                return $this->prepareErrorResponse('Forbidden', 'AuthorizationHttpException', 403);
            }
            if ($e instanceof UnauthorizedHttpException) {
                return $this->prepareErrorResponse('Unauthorized', 'UnauthorizedHttpException', 401);
            }
            if ($e instanceof ResourceException) {
                return $this->prepareErrorResponse($e->getMessage(), ResourceException::class, 409);
            }
        }
        if ($e instanceof UnauthorizedHttpException) {
            return redirect()->guest('login');
        }

        return parent::render($request, $e);
    }

    protected function prepareErrorResponse($message, $type, $code)
    {
        return new JsonResponse([
            'error' => [
                'message' => $message,
                'type' => $type,
                'code' => $code,
            ],
        ], $code);
    }
}
