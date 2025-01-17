export type DesignOriginType = "create" | "file" | "url" | "registry";

export interface Design {

    id: string;
    type: string;
    name: string;
    description: string|undefined;
    createdBy: string;
    createdOn: Date;
    modifiedBy: string;
    modifiedOn: Date;
    origin: DesignOriginType;

}
