package com.example.mangalibrary.Mocks;

import android.util.Log;

import com.example.mangalibrary.Models.Noticia;

import java.io.Serializable;
import java.util.ArrayList;

public class NoticiasDAO implements Serializable {

    ArrayList<Noticia> noticias;

    public NoticiasDAO () {
        this.noticias = new ArrayList<Noticia>();
        this.noticias.add(new Noticia("Yu Yu Hakusho: Live-action da Netflix ganha trailer",
                "https://www.jbox.com.br/wp/wp-content/uploads/2023/08/yusuke-urameshi-destacada.jpg",
                "Com estreia marcada para 14 de dezembro, a série live-action de Yu Yu Hakusho finalmente ganhou um trailer pela Netflix. Confira:\n" +
                        "A produção é e Akira Morii pelo estúdio ROBOT (Alice in Borderland) e Kazutaka Mori ficou como produtor executivo — a Netflix alugou estúdios na TOHO para as filmagens.\n" +
                        "\n" +
                        "No elenco, temos:\n" +
                        "\n" +
                        "Takumi Kitamura (Arashi em Tokyo Revengers live-action) como Yusuke Urameshi,\n" +
                        "Shuhei Uesugi (Rui Nikaido em Hotel Concierge) como Kazuma Kuwabara,\n" +
                        "Kanata Hongo (Ryoma Echizen no live-action de The Prince of Tennis, Shin em Nana 2) como Hiei,\n" +
                        "Jun Shison (Right/ToQ1gou em ToQger, Yukiatsu em Anohana) como Kurama.\n" +
                        "\n" +
                        "\n" +
                        "Olá, caso você esteja usando este texto para o seu site/conteúdo, lembre-se de nos mandar um salve. Como somos independentes, todo tipo de reconhecimento é muito bem-vindo. Muito obrigado!\n" +
                        "--Equipe JBox"));
        this.noticias.add(new Noticia("‘Jaspion’ é exibido dublado no canal SATO+ da SamsungTV",
                "https://www.jbox.com.br/wp/wp-content/uploads/2023/04/jaspionbanner.jpg",
                "Quem tem SamsungTV pode assistir a Jaspion dublado na programação do canal linear Sato+ — não sabemos exatamente quando a série chegou dublada, mas está sendo exibida por lá em qualidade mais alta via upscaling. O Sato+ está nos canais da Samsung desde março de 2023.\n" +
                        "\n" +
                        "A Sato Company tirou a versão dublada do ar há alguns anos devido a questões contratuais e processuais, e alegou estar resolvendo questões de direitos conexos. Em outros serviços, como o Prime Video, Jaspion permanece no ar legendado. Com isso, espera-se que a dublagem entre no ar em outras plataformas em breve.\n" +
                        "\n" +
                        "Olá, caso você esteja usando este texto para o seu site/conteúdo, lembre-se de nos mandar um salve. Como somos independentes, todo tipo de reconhecimento é muito bem-vindo. Muito obrigado!\n" +
                        "--Equipe JBox"));
        this.noticias.add(new Noticia("Cavaleiros do Zodíaco: Iron Studios abre pré-venda de estátua do Shun de Andrômeda",
                "https://www.jbox.com.br/wp/wp-content/uploads/2023/11/shunironstudios-destacada.jpg",
                "No fim do mês passado, a Iron Studios, empresa brasileira dedicada à produção de estatuetas colecionáveis, abriu mais uma pré-venda da sua coleção baseada em Os Cavaleiros do Zodíaco. Depois do Grande Mestre, Hyoga e Seiya, é a vez do Shun de Andrômeda — que assim como seus colegas bronzeados, vem trajando a primeira armadura vista no animê clássico (a famosa “V1”).\n" +
                        "\n" +
                        "Também como de padrão da coleção, há duas versões: a “regular”, apenas com o personagem, e a “deluxe”, que inclui uma figura da constelação de Andrômeda, que pode acender com uma luz de LED. Vale destacar que o processo de aprovação ainda está em andamento, sendo possível que pequenas alterações sejam feitas até o produto final ficar pronto. A entrega está prevista para o terceiro trimestre de 2024.\n" +
                        "\n" +
                        "A reserva é feita com um voucher de entrada, que ainda não é o valor final do produto, sendo um sinal de R$ 100,00 para a versão simples e R$ 200,00 para a versão deluxe. O produto final está estimado em 169,99 dólares na figura padrão e 399,99 dólares na deluxe (podendo o preço final variar conforme câmbio e outras questões).\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Olá, caso você esteja usando este texto para o seu site/conteúdo, lembre-se de nos mandar um salve. Como somos independentes, todo tipo de reconhecimento é muito bem-vindo. Muito obrigado!\n" +
                        "--Equipe JBox"));
    }

    public ArrayList<Noticia> getNoticias () {
        return this.noticias;
    }

    public void adicionarNoticia (String titulo, String imagem, String texto) {
        noticias.add(new Noticia(titulo,imagem,texto));
        Log.e("ADD","Uma notícia foi adicionada.");
    }
}