#include <stdio.h>
#include <stdlib.h>

int main(void) {
int parametro;
int fatorial;
float ola;
	scanf("%i",&parametro);
	scanf("%f",&ola);
	fatorial = 13000515;
	ola = 2147483647.0;
	if(parametro==0){
	fatorial = 1 + 20;
	}
	while(parametro>1){
		fatorial = fatorial *  (parametro - 1);
		parametro = parametro - 1;
	}
	printf("%i\n",fatorial);
	printf("%f\n",ola);
	printf("%s\n","Oi");
return 0;
}
