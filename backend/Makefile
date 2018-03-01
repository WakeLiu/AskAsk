PHP = docker-compose exec -T php

all:

migrate:
	$(PHP) php artisan migrate

rollback:
	$(PHP) php artisan migrate:rollback

test:
	$(PHP) vendor/bin/phpunit

seed:
	$(PHP) php artisan db:seed

log:
	tail -f storage/logs/laravel.log

init:
	$(PHP) composer install --no-interaction --prefer-dist
	$(PHP) php artisan key:generate
	$(PHP) php artisan jwt:secret
	make migrate
