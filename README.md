Proiect GlobalWaves - Etapa 2 - Pagination

      Pentru implementarea acestei teme am folosit scheletul oferit de echipa OOP
   Implementarea temei am incercat sa o fac cat mai apropiata de ideea scheletului
   anterior  ceea ce m a ajutat sa adaug noi functionalitati
   la search, select, load, etc... fara a face schimbari majore in cod.

      AllUsers:
   Stiind ca Hostii si Artistii sunt Useri care au niste
   proprietati in plus, am hotarat sa folosesc mosternire,
   ceea ce mi a permis sa i adaug pe toti intr o lista generala de useri.
   Mi am implementat si metode ajutatoare, pentru a primi doar
   Hostii/Userii pentru unele functionalitati.
   Apoi se apeleaza metodele, in urma carora 
   se face override verificand daca userul este de tipul dorit.
   Pentru a adauga albume se verifica daca mai exista vreun album al Artistului 
   cu asa nume si daca
   mai exista vreo melodie cu acelasi nume.
   Pentru a adauga un event am folosit chatgpt,
   pentru a intelege si pentru a ma ajuta la dateFormat.
   Pentru a ii atribui unui user o pagina, se verifica tipul lui,
   il creeam si dupa acest lucru i se atribuie o pagina noua
   de tipul specificat in conditie, in fiecare pagina avem o metoda de printare.
   Cand se creaza userulam folosit si un FACTORY PATTERN.
   Odaca ce un user cauta un artist/host si ii selecteaza pagina
   , automat currentpage ului sau i se atribuie pagina userului selectat,
   daca ulterior se apeleaza printarea paginii,
   se afiseaza datele necesare. Daca userul doreste sa schimbe pagina atunci,
   in dependenta de nextPage ul dorit i se atribuie currentPage
    ului sau pagina noua dorita.
   Pentru functia delete user se verifica player ul tuturor userilor
   -  Daca este pe pauza putem sterge userul, altfel nu.
   -  Daca canta vreo melodie a artistului, podcast al hostului, sau vreo melodie
   din playlist al userului simplu la cineva din utilizatori, nu il puteam sterge.
   -  O noua verificare care se atribuie doar Hostilor si Artistilor
   este cea de a verifica
   daca vreun user se afla pe pagina sa, iteram prin toti utilizatorii si verificam
   daca este macar un user in afara de cel pe care dorim sa l stergem
   care are la currentPage pagina celui ce va disparea :))
   In acest Readme am explicat doar functiile mai grele al etapei
   si cum le am gandit, celelalte fiind scurte si avand comentarii in cod.

      Cu drag, Vrinceanu Dan 325CDa