import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { ArtistaService, TaskService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import Artista from 'Frontend/generated/com/unl/clasesestructura/base/models/Artista';
import { useEffect, useState } from 'react';


export const config: ViewConfig = {
  title: 'Artistas',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 3,
    title: 'Artistas',
  },
};

// Formulario para registrar nuevo artista
type ArtistaEntryFormProps = {
  onArtistaCreated?: () => void;
};

// Formulario para actualizar artista
type ArtistaEntryFormUpdateProps ={
  id: number;
  nombre: string;
  nacionalidad: string;
  onArtistaUpdated?: () => void;
};

//GUARDAR ARTISTA
function ArtistaEntryForm(props: ArtistaEntryFormProps) {
  const nombre = useSignal('');
  const nacionalidad = useSignal('');
  
  const createArtista = async () => {
    try {
      if (nombre.value.trim().length > 0 && nacionalidad.value.trim().length > 0) {
        await ArtistaService.createArtista(nombre.value, nacionalidad.value);
        if (props.onArtistaCreated) {
          props.onArtistaCreated();
        }
        nombre.value = '';
        nacionalidad.value = '';
        dialogOpened.value = false;
        Notification.show('Artista creado', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo crear, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }

    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };
  
  let pais = useSignal<String[]>([]);
  useEffect(() => {
    ArtistaService.listCountry().then(data =>
      pais.value = data
    );
  }, []);
  const dialogOpened = useSignal(false);
  return (
    <>
      <Dialog
        modeless
        headerTitle="Nuevo artista"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => {
          dialogOpened.value = detail.value;
        }}
        footer={
          <>
            <Button
              onClick={() => {
                dialogOpened.value = false;
              }}
            >
              Cancelar
            </Button>
            <Button onClick={createArtista} theme="primary">
              Registrar
            </Button>
            
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre del artista" 
            placeholder="Ingrese el nombre del artista"
            aria-label="Nombre del artista"
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
          />
          <ComboBox label="Nacionalidad" 
            items={pais.value}
            placeholder='Seleccione un pais'
            aria-label='Seleccione un pais de la lista'
            value={nacionalidad.value}
            onValueChanged={(evt) => (nacionalidad.value = evt.detail.value)}
            />
        </VerticalLayout>
      </Dialog>
      <Button
            onClick={() => {
              dialogOpened.value = true;
            }}
          >
            Agregar
          </Button>
    </>
  );
}


// Update artista
function ArtistaEntryFormUpdate(props: ArtistaEntryFormUpdateProps) {
  const dialogOpened = useSignal(false);
  const nombre = useSignal(props.nombre);
  const nacionalidad = useSignal(props.nacionalidad);
  const ident = useSignal(props.id);

  // Sincroniza los valores cuando cambian las props
  useEffect(() => {
    nombre.value = props.nombre;
    nacionalidad.value = props.nacionalidad;
    ident.value = props.id;
  }, [props.nombre, props.nacionalidad, props.id]);


  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };

  const updateArtista = async () => {
    try {
      if (nombre.value.trim().length > 0 && nacionalidad.value.trim().length > 0) {
        await ArtistaService.updateArtista(parseInt(ident.value), nombre.value, nacionalidad.value);
        if (props.onArtistaUpdated) {
          props.onArtistaUpdated();
        }
        dialogOpened.value = false;
        Notification.show('Artista actualizado', { 
          duration: 5000, 
          position: 'bottom-end', 
          theme: 'success' 
        });
      } else {
        Notification.show('No se pudo actualizar, faltan datos', { 
          duration: 5000, 
          position: 'top-center', 
          theme: 'error' 
        });
      }
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  let pais = useSignal<String[]>([]);
  useEffect(() => {
    ArtistaService.listCountry().then(data => pais.value = data);
  }, []);

  return (
    <>
      <Dialog
        aria-label="Editar Artista"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => {
          dialogOpened.value = event.detail.value;
        }}
        header={
          <h2
            className="draggable"
            style={{
              flex: 1,
              cursor: 'move',
              margin: 0,
              fontSize: '1.5em',
              fontWeight: 'bold',
              padding: 'var(--lumo-space-m) 0',
            }}
          >
            Editar Artista
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={updateArtista}>
              Actualizar
            </Button>
          </>
        )}
      >
        <VerticalLayout
          theme="spacing"
          style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}
        >
          <VerticalLayout style={{ alignItems: 'stretch' }}>
            <TextField 
              label="Nombre del artista" 
              placeholder="Ingrese el nombre del artista"
              aria-label="Nombre del artista"
              value={nombre.value}
              onValueChanged={(evt) => (nombre.value = evt.detail.value)}
            />
            <ComboBox 
              label="Nacionalidad" 
              items={pais.value}
              placeholder='Seleccione un país'
              aria-label='Seleccione un país de la lista'
              value={nacionalidad.value}
              onValueChanged={(evt) => (nacionalidad.value = evt.detail.value)}
            />
          </VerticalLayout>
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Editar</Button>
    </>
  );
}

////********************************* */

//LISTA DE ARTISTAS
export default function ArtistaView() {
  
  const dataProvider = useDataProvider<Artista>({
    list: () => ArtistaService.listAll(),
  });

function indexLink({ item }: { item: Artista }) {
  return (
    <span>
      <ArtistaEntryFormUpdate
        id={item.id}
        nombre={item.nombre}
        nacionalidad={item.nacionalidad}
        onArtistaUpdated={dataProvider.refresh}
      />
    </span>
  );
}

  function indexIndex({model}:{model:GridItemModel<Artista>}) {
    return (
      <span>
        {model.index + 1} 
      </span>
    );
  }

  return (

    <main className="w-full h-full flex flex-col box-border gap-s p-m">

      <ViewToolbar title="Lista de artista">
        <Group>
          <ArtistaEntryForm onArtistaCreated={dataProvider.refresh}/>
        </Group>
      </ViewToolbar>
      <Grid dataProvider={dataProvider.dataProvider}>
        <GridColumn  renderer={indexIndex} header="Nro" />
        <GridColumn path="nombre" header="Nombre del artista" />
        <GridColumn path="nacionalidad" header="Nacionalidad">

        </GridColumn>
        <GridColumn header="Acciones" renderer={indexLink}/>
      </Grid>
    </main>
  );
}