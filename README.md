# FileHandler
<h2>Тестовое задание на вакансию Node.js разработчик</h2>

<h3>Условие:</h3>
Имеется файл размером 1 тб, состоящий из строк. 

<h3>Задача:</h3>
Нужно написать программу, которая сможет отсортировать этот файл на машине, которой
доступно 500мб ОЗУ.

<h3>Решение:</h3>
Для решения данной задачи был выбран язык программирования Java и IDE Visual Studio Code.<br>
В данной программе файл сортируется внешне - на жестком диске, избегая полного хранения в оперативной памяти. Он разбивается на некоторое множество маленьких частей и только после этого происходит сортировка целых чисел по возрастанию. Программа разрабатывалась для txt файлов такого формата записи, в котором одно число хранится в одной строке, следующее - в следующей:<br>
123<br>
12342124<br>
15324542<br>
3<br>
2<br>
23242<br>
