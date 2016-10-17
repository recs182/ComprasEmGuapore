package br.com.macropublicidade.comprasemguapore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.macropublicidade.comprasemguapore.models.Company;
import br.com.macropublicidade.comprasemguapore.models.Group;
import br.com.macropublicidade.comprasemguapore.models.Sector;

public class CompanyActivity extends AppCompatActivity implements View.OnClickListener{

    private String[] url_banner = new String[]{"http://www.comprasemguapore.com.br/images/companies/banners/", "_banner.jpg"};
    private String[] url_logo = new String[]{"http://www.comprasemguapore.com.br/images/companies/logos/", "_logo.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        final Bundle extras = getIntent().getExtras();
        Group group = (Group) extras.getSerializable("group");
        Sector sector = (Sector) extras.getSerializable("sector");
        Company company = (Company) extras.getSerializable("company");

        setTitle( company.getCompany() );

        ImageView banner = (ImageView) findViewById(R.id.CompanyActivity_banner);
        ImageView logo = (ImageView) findViewById(R.id.CompanyActivity_logo);

        TextView title = (TextView) findViewById(R.id.CompanyActivity_company);
        TextView sector_name = (TextView) findViewById(R.id.CompanyActivity_sector);
        TextView phone = (TextView) findViewById(R.id.CompanyActivity_phone);
        TextView description = (TextView) findViewById(R.id.CompanyActivity_description);

        Button website = (Button) findViewById(R.id.CompanyActivity_webpage);
        Button facebook = (Button) findViewById(R.id.CompanyActivity_facebook);
        Button instagram = (Button) findViewById(R.id.CompanyActivity_instagram);
        Button whatsapp = (Button) findViewById(R.id.CompanyActivity_whatsapp);

        website.setOnClickListener(this);
        facebook.setOnClickListener(this);
        instagram.setOnClickListener(this);
        whatsapp.setOnClickListener(this);

        //TODO: download banner to phone, check if image exists in phone and load it
        //TODO: load logos in ListCompaniesActivity
        Picasso.with(this).load(url_banner[0] + company.getId().toString() + url_banner[1]).fit().into(banner);
        Picasso.with(this).load(url_logo[0] + company.getId().toString() + url_logo[1]).fit().placeholder(R.drawable.default_logo).into(logo);

        title.setText( company.getCompany() );
        if( group.getName().equals("Único") ){
            sector_name.setText( sector.getName() );
        }else{
            sector_name.setText( group.getName() );
        }

        this.textviewText( description, company.getDescription() );

        this.buttonVisibility( website, company.getWebpage() );
        this.buttonVisibility( facebook, company.getFacebook() );
        this.buttonVisibility( instagram, company.getInstagram() );
        this.buttonVisibility( whatsapp, company.getWhatsapp() );
    }

    private void textviewText( TextView view, String value ){
        String tested = this.preventNullValue( value );
        if( tested.isEmpty() ){
            view.setVisibility(TextView.GONE);
        }else{
            view.setText( value );
        }
    }

    private void buttonVisibility( Button btn, String value ){
        String tested = this.preventNullValue( value );
        if( tested.isEmpty() ){
            btn.setVisibility(Button.GONE);
        }
    }

    private String preventNullValue( String value ){
        if( value == null || value.equals("null") ) {
            return "";
        }
        return value;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.CompanyActivity_webpage:
                break;
            case R.id.CompanyActivity_facebook:
                break;
            case R.id.CompanyActivity_instagram:
                break;
            case R.id.CompanyActivity_whatsapp:
                break;
        }
    }

}
