package com.example.mervshops.Adapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.namespace.R;
public class ViewPageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    public ViewPageAdapter(Context context){
        this.context=context;
    }
    private int images[]={
            R.drawable.sans_titre,
            R.drawable.poulet2,
            R.drawable.polts3
    };
    private String titles[]={
            "Livraison de poulets frais directement à votre porte",
            "Commandez facilement vos à partir de notre application",
            "Livraison gratuite et rapide"
    };
    private String desc[]={
            "Bienvenue sur notre application de livraison de poulets frais à domicile. Notre service de livraison vous permet d'accéder facilement à des poulets de qualité supérieure tout en vous épargnant le temps et les tracas de vous rendre au supermarché pour les acheter. Nous offrons des options de livraison flexibles et plusieurs choix de poulets frais soigneusement sélectionnés pour satisfaire vos besoins.",
            "À partir de notre application, commander du poulet frais est un jeu d'enfant. Sélectionnez le poids souhaité, choisissez une date de livraison qui vous convient et donnez-nous les informations de votre adresse pour que nous puissions expédier votre commande. Des prix abordables, une livraison rapide et des poulets de qualité supérieure sont ce qui nous distingue des autres applications de livraison de nourriture.",
            "Chez nous, l'expédition de votre commande n'est pas seulement rapide mais gratuite. Nous livrons votre poulet frais directement à votre porte à une date qui convient le mieux à votre emploi du temps. Vous pouvez être assuré que votre commande arrivera fraîche et prête à cuisiner dès qu'elle sera entre vos mains. Commandez dès aujourd'hui et faites l'expérience de notre service de livraison de poulets frais."
    };
    @Override
    public int getCount(){
        return titles.length;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(LinearLayout)object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container,int position){
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.view_page,container,false);
        //initview
        ImageView imageView=v.findViewById(R.id.imageView);
        TextView textTitle=v.findViewById(R.id.textTitleViewPage);
        TextView textSubTitle=v.findViewById(R.id.textSubTitleViewPage);
        imageView.setImageResource(images[position]);
        textTitle.setText(titles[position]);
        textSubTitle.setText(desc[position]);

        container.addView(v);
        return v;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container,int position, @NonNull Object object){
        container.removeView((LinearLayout)object);
    }
}
