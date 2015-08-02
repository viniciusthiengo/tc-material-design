TCMaterialDesign APP
===========================================

[English version](https://github.com/viniciusthiengo/tc-material-design/blob/master/README-EN.md)

## Projeto ##

TCMaterialDesign é um APP desenvolvido nos exemplos em vídeo do [canal do YouTube Thiengo Calopsita](https://www.youtube.com/user/thiengoCalopsita) com intuito de apresentar a Android developers algumas features / libs que podem ser utilizadas no dev Android.

![PrintScreen APP exemplo](https://s3-sa-east-1.amazonaws.com/thiengo-calopsita/github/device-2015-06-07-122119.png)
![PrintScreen APP exemplo](https://s3-sa-east-1.amazonaws.com/thiengo-calopsita/github/device-2015-07-19-211451.png)

A principio a APP foi criada apenas para cobrir a série Material Design no Android que estva em voga no canal, porém para aproveitar a continuidade no dev de um APP real e então dar aos seguidores do canal / Blog uma visão de um APP sendo desenvolvido do zero e com todos os vídeos disponíveis essa APP passou a ser a entidade para implementação de exemplos além da série Material Design no Android.

*Note que a APP está sendo desenvolvida de acordo com o passar dos vídeos, logo pode acontecer de você baixar o código e ter algumas entidades em lugares não esperados ou até mesmo com possíveis bugs impedindo a execução da APP. Esse tipo de situação provavelmente foi explicado no último vídeo liberado pelo canal e quando será corrigido.*

## Instalação / Importação ##

Para rodar o projeto basta obter o `código` por clone ou realizando o download do `.zip`. Então no Android Studio (depois de descompactado o projeto, caso tenha importado via .zip), logo na página inicial, clique em "import" e navegue até o diretório do projeto selecionando-o. Dessa forma o projeto deve ser carregado sem problemas no AndroidStudio. É importante que você tenha o AndroidStudio atualizado, pois a APP é desenvolvida sempre com a última versão do IDE e do SDK.

### Server-Side ###

Desde o vídeo [Volley, Gson e RetryPolicy em Material Design Android Série APP](http://www.thiengo.com.br/volley-gson-e-retrypolicy-em-material-design-android-serie-app) a APP passou a contar com um script no lado servidor do sistema, script desenvolvido em PHP que se comunica com o SGBD MySQL para obtenção da lista de carros do APP. O servidor é de sua preferencia, apesar de os testes terem utilizado o Apache 2.2. O repositório desse script do lado servidor pode ser econtrado em [tc-material-design-web](https://github.com/viniciusthiengo/tc-material-design-web)

## Requerimentos ##

* AndroidStudio 1.+

### Suporte ###

* minSdkVersion 14

### Libraries / Compiles ###

Abaixo a lista das libraries / compiles que estão sendo utilizados no projeto:

* com.android.support:appcompat-v7:22.2.0
* com.android.support:cardview-v7:22.2.0
* com.android.support:recyclerview-v7:22.2.0
* com.nineoldandroids:library:2.4.0
* com.daimajia.easing:library:1.0.1@aar
* com.daimajia.androidanimations:library:1.1.3@aar
* com.mikepenz.materialdrawer:library:2.8.2@aar
* com.melnykov:floatingactionbutton:1.3.0
* me.drakeet.materialdialog:library:1.2.2
* com.android.support:design:22.2.0
* com.facebook.fresco:fresco:0.5.1+
* com.github.bumptech.glide:glide:3.6.0
* de.greenrobot:eventbus:2.4.0
* com.google.android.gms:play-services:7.5.+
* com.wdullaer:materialdatetimepicker:1.4.1
* com.mcxiaoke.volley:library:1.0.17
* com.google.code.gson:gson:2.3.1

# Onde acompanhar o conteúdo do Blog? #

* [Blog Thiengo Calopsita](http://www.thiengo.com.br/)
* [YouTube](https://www.youtube.com/user/thiengoCalopsita)
* [Facebook](https://www.facebook.com/thiengoCalopsita)
* [Google+](https://plus.google.com/+ThiengoCalopsita/posts)
* [Twitter](https://twitter.com/thiengoCalops)
* [LinkedIn](https://www.linkedin.com/pub/vin%C3%ADcius-thiengo/80/9b1/517)

Tem também a APP do Blog:

* [APP Blog Thiengo Calopsita](https://play.google.com/store/apps/details?id=br.thiengocalopsita&hl=pt_BR)

Sinta-se livre para enviar qualquer dica, correção, ... se possível, em caso de uma solução encontrada para um possível problema informado em algum dos vídeos, coloque também na área de comentários do Blog a solução encontrada, assim ajuda a todos que tiverem o mesmo problema.