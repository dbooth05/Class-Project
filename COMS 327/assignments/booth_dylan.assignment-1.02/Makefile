pokemon: pokemon.o queue.o
	gcc pokemon.o queue.o -o pokemon -lm

pokemon.o: pokemon.c queue.h pokemon.h
	gcc -Wall -Werror -ggdb pokemon.c -c 

queue.o: queue.c queue.h pokemon.h
	gcc -Wall -Werror -ggdb queue.c -c

clean:
	rm -f pokemon pokemon.o queue.o