package com.axiom.operatio.model.gameplay;

import com.axiom.operatio.utils.JSONSerializable;
import com.axiom.operatio.model.materials.Material;
import com.axiom.operatio.model.production.machine.MachineType;
import com.axiom.operatio.model.production.machine.Operation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Права доступные игроку
 */
public class GamePermissions implements JSONSerializable {

    private ArrayList<Material> availableMaterials;
    private ArrayList<MachineType> availableMachines;
    private ArrayList<Operation> availableOperations;
    private long lastChangeTime;

    //------------------------------------------------------------------------------------

    public GamePermissions() {
        availableMachines = new ArrayList<>();
        availableMaterials = new ArrayList<>();
        availableOperations = new ArrayList<>();
        lastChangeTime = 0;
    }


    public GamePermissions(JSONObject permissions) {
        this();
        try {
            JSONArray jsonMaterials = permissions.getJSONArray("availableMaterials");
            JSONArray jsonMachines = permissions.getJSONArray("availableMachines");
            JSONArray jsonOperations = permissions.getJSONArray("availableOperations");
            for (int i=0; i<jsonMaterials.length(); i++) {
                int ID = jsonMaterials.getInt(i);
                availableMaterials.add(Material.getMaterial(ID));
            }
            for (int i=0; i<jsonMachines.length(); i++) {
                int ID = jsonMachines.getInt(i);
                availableMachines.add(MachineType.getMachineType(ID));
            }
            for (int i=0; i<jsonOperations.length(); i++) {
                int ID = jsonOperations.getInt(i);
                availableOperations.add(Operation.getOperation(ID));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------

    public void clear() {
        availableMaterials.clear();
        availableMachines.clear();
        availableOperations.clear();
    }

    //------------------------------------------------------------------------------------

    public boolean addMaterialPermission(Material material) {
        if (material==null) return false;
        if (availableMaterials.contains(material)) return true;
        lastChangeTime = System.nanoTime();
        return availableMaterials.add(material);
    }

    public boolean addMachinePermission(MachineType machine) {
        if (machine==null) return false;
        if (availableMachines.contains(machine)) return true;
        lastChangeTime = System.nanoTime();
        return availableMachines.add(machine);
    }


    public boolean addOperationPermission(Operation operation) {
        if (operation==null) return false;
        if (availableOperations.contains(operation)) return true;
        lastChangeTime = System.nanoTime();
        return availableOperations.add(operation);
    }

    //------------------------------------------------------------------------------------

    public long getLastChangeTime() {
        return lastChangeTime;
    }

    public boolean isAvailable(Material material) {
        if (material==null) return false;
        return availableMaterials.contains(material);
    }

    public boolean isAvailable(MachineType machine) {
        if (machine==null) return false;
        return availableMachines.contains(machine);
    }

    public boolean isAvailable(Operation operation) {
        if (operation==null) return false;
        return availableOperations.contains(operation);
    }

    //------------------------------------------------------------------------------------
    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonMaterials = new JSONArray();
        JSONArray jsonMachines = new JSONArray();
        JSONArray jsonOperations = new JSONArray();
        for (int i=0; i<availableMaterials.size(); i++)
            jsonMaterials.put(availableMaterials.get(i).getMaterialID());

        for (int i=0; i<availableMachines.size(); i++)
            jsonMachines.put(availableMachines.get(i).getID());

        for (int i=0; i<availableOperations.size(); i++)
            jsonOperations.put(availableOperations.get(i).getID());

        try {
            jsonObject.put("availableMaterials", jsonMaterials);
            jsonObject.put("availableMachines", jsonMachines);
            jsonObject.put("availableOperations", jsonOperations);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
