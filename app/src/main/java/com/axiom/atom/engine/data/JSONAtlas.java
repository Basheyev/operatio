package com.axiom.atom.engine.data;

import android.content.res.Resources;

import com.axiom.atom.engine.graphics.gles2d.Texture;
import com.axiom.atom.engine.graphics.gles2d.TextureAtlas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Загружает TextureAtlas из JSon (поддерживает JSONArray, no trimming, no rotating)
 */
public class JSONAtlas {

    public static TextureAtlas loadTextureAtlas(Resources resources, int imageID, int atlasID) {
        Texture texture = Texture.getInstance(resources, imageID);
        String json = loadTextFile(resources, atlasID);
        return parseJSonArray(texture, json);
    }


    protected static String loadTextFile(Resources resources, int atlasID) {
        BufferedReader input = null;
        StringBuilder data = new StringBuilder(16384);
        String line;
        try {
            input = new BufferedReader(new InputStreamReader(resources.openRawResource(atlasID)));
            while ((line = input.readLine()) != null) {
                data.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }


    protected static TextureAtlas parseJSonArray(Texture texture, String jsonData) {
        TextureAtlas atlas = new TextureAtlas(texture);
        String name;
        int x, y, width, height;

        try {
            JSONObject jsonAtlas = new JSONObject(jsonData);
            JSONArray regions = jsonAtlas.getJSONArray("frames");
            for (int i = 0; i < regions.length(); i++) {
                JSONObject region = regions.getJSONObject(i);
                name = region.getString("filename");
                JSONObject frame = region.getJSONObject("frame");
                x = frame.getInt("x");
                y = frame.getInt("y");
                width = frame.getInt("w");
                height = frame.getInt("h");
                atlas.addRegion(name, x, y, width, height);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return atlas;
    }


}


/*
{"frames": [
    {
        "filename": "128x128",
        "frame": {"x":893,"y":342,"w":128,"h":128},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":128,"h":128},
        "sourceSize": {"w":128,"h":128},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "advanced_wars_land",
        "frame": {"x":132,"y":641,"w":320,"h":48},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":320,"h":48},
        "sourceSize": {"w":320,"h":48},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "contra2",
        "frame": {"x":2,"y":316,"w":142,"h":222},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":142,"h":222},
        "sourceSize": {"w":142,"h":222},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "contra3",
        "frame": {"x":645,"y":197,"w":246,"h":201},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":246,"h":201},
        "sourceSize": {"w":246,"h":201},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "diamonds32x5",
        "frame": {"x":585,"y":596,"w":318,"h":49},
        "rotated": false,
        "trimmed": true,
        "spriteSourceSize": {"x":1,"y":15,"w":318,"h":49},
        "sourceSize": {"w":320,"h":64},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "exocet_spaceman",
        "frame": {"x":146,"y":316,"w":153,"h":175},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":153,"h":175},
        "sourceSize": {"w":153,"h":175},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "explosion",
        "frame": {"x":2,"y":2,"w":319,"h":312},
        "rotated": false,
        "trimmed": true,
        "spriteSourceSize": {"x":1,"y":6,"w":319,"h":312},
        "sourceSize": {"w":320,"h":320},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "helix",
        "frame": {"x":724,"y":472,"w":221,"h":28},
        "rotated": false,
        "trimmed": true,
        "spriteSourceSize": {"x":6,"y":0,"w":221,"h":28},
        "sourceSize": {"w":233,"h":30},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "knight3",
        "frame": {"x":323,"y":204,"w":320,"h":131},
        "rotated": false,
        "trimmed": true,
        "spriteSourceSize": {"x":0,"y":0,"w":320,"h":131},
        "sourceSize": {"w":320,"h":200},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "lance-overdose-loader-eye",
        "frame": {"x":2,"y":540,"w":128,"h":128},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":128,"h":128},
        "sourceSize": {"w":128,"h":128},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "mask-test",
        "frame": {"x":323,"y":2,"w":320,"h":200},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":320,"h":200},
        "sourceSize": {"w":320,"h":200},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "metalslug_monster39x40",
        "frame": {"x":436,"y":337,"w":156,"h":160},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":156,"h":160},
        "sourceSize": {"w":156,"h":160},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "pacman_by_oz_28x28",
        "frame": {"x":454,"y":647,"w":308,"h":28},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":308,"h":28},
        "sourceSize": {"w":308,"h":28},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "parsec",
        "frame": {"x":281,"y":527,"w":302,"h":80},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":302,"h":80},
        "sourceSize": {"w":302,"h":80},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "profil-sad-plush",
        "frame": {"x":146,"y":493,"w":133,"h":142},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":133,"h":142},
        "sourceSize": {"w":133,"h":142},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "saw",
        "frame": {"x":594,"y":400,"w":128,"h":128},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":128,"h":128},
        "sourceSize": {"w":128,"h":128},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "shocktroopers-lulu2",
        "frame": {"x":301,"y":337,"w":133,"h":188},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":133,"h":188},
        "sourceSize": {"w":133,"h":188},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "snowflakes_large",
        "frame": {"x":585,"y":530,"w":379,"h":64},
        "rotated": false,
        "trimmed": true,
        "spriteSourceSize": {"x":2,"y":0,"w":379,"h":64},
        "sourceSize": {"w":384,"h":64},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "spaceman",
        "frame": {"x":724,"y":502,"w":225,"h":16},
        "rotated": false,
        "trimmed": true,
        "spriteSourceSize": {"x":15,"y":0,"w":225,"h":16},
        "sourceSize": {"w":240,"h":16},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "steelpp-font",
        "frame": {"x":645,"y":2,"w":320,"h":193},
        "rotated": false,
        "trimmed": true,
        "spriteSourceSize": {"x":0,"y":0,"w":320,"h":193},
        "sourceSize": {"w":320,"h":200},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "treasure_trap",
        "frame": {"x":893,"y":197,"w":127,"h":143},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":127,"h":143},
        "sourceSize": {"w":127,"h":143},
        "pivot": {"x":0.5,"y":0.5}
    },
    {
        "filename": "vu",
        "frame": {"x":281,"y":609,"w":300,"h":30},
        "rotated": false,
        "trimmed": false,
        "spriteSourceSize": {"x":0,"y":0,"w":300,"h":30},
        "sourceSize": {"w":300,"h":30},
        "pivot": {"x":0.5,"y":0.5}
    }],
    "meta": {
        "app": "http://www.codeandweb.com/texturepacker",
        "version": "1.0",
        "image": "megaset-3.png",
        "format": "RGBA8888",
        "size": {"w":1023,"h":691},
        "scale": "1",
        "smartupdate": "$TexturePacker:SmartUpdate:5e8f90752cfd57d3adfb39bcd3eef1b6:87d98cec6fa616080f731b87726d6a1e:b55588eba103b49b35a0a59665ed84fd$"
    }
};
 */